package com.yscp.m3u8util;

import com.yscp.m3u8util.helper.FileUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class FileDownloadStarter {

    private static ExecutorService es =  Executors.newFixedThreadPool(40);

    public static void doBegins(String url) throws IOException, InterruptedException {
        if(!(url.startsWith("http://") || url.startsWith("https://")) || !url.endsWith(".m3u8")) {
            throw new IllegalArgumentException("地址不正确，请确认");
        }
        String urlPre = url.substring(0, url.lastIndexOf("/"));
        String urlSub = url.substring(url.lastIndexOf("/"));

        List<String> fileList = (new M3u8UrlHandler()).parse2FileList(urlPre, urlSub);

        File file = new File("D:/tmp");
        FileUtil.deleteDir(file);
        System.out.println("清空目录：D:/tmp");
        if(!file.exists()) {
            file.mkdir();
        }

        CountDownLatch countDownLatch = new CountDownLatch(fileList.size());
        for (int i = 0; i < fileList.size(); i++) {
            SingleFileDownloader task = new SingleFileDownloader(fileList.get(i),i, countDownLatch);
            es.submit(task);
        }
        countDownLatch.await();
        System.out.println("分片下载完成，开始合并文件");
        FileUtil.mergeFiles("D:/xxx.ts", "D:/tmp/");
        System.out.println("合并完成, 请查看D:/xxx.ts并重命名");
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        FileDownloadStarter.doBegins(args[0]);
        es.shutdown();
        System.exit(0);
    }

}

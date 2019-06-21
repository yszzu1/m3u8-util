package com.yscp.m3u8util;

import com.yscp.m3u8util.helper.NumberUtil;
import okhttp3.*;
import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class SingleFileDownloader implements Runnable {

    private String fileUrl;

    private Integer fileIndex;

    private CountDownLatch countDownLatch;

    public SingleFileDownloader(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public SingleFileDownloader(String fileUrl, CountDownLatch countDownLatch) {
        this.fileUrl = fileUrl;
        this.countDownLatch = countDownLatch;
    }

    public SingleFileDownloader(String fileUrl, Integer fileIndex, CountDownLatch countDownLatch) {
        this.fileUrl = fileUrl;
        this.fileIndex = fileIndex;
        this.countDownLatch = countDownLatch;
    }

    private OkHttpClient okHttpClient = new OkHttpClient();


    public void doDownload() throws IOException, InterruptedException {

        String fileName = fileUrl;
        if (fileUrl.startsWith("http://") || fileUrl.startsWith("https://")) {
            fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        }

        fileName = "D:/tmp/" + NumberUtil.extent2String(fileIndex, 6) + fileName;

        boolean needRedo = true;
        int redoTimes = 0;
        try {
            while (needRedo) {
                Request.Builder builder = new Request.Builder();
                builder.url(this.fileUrl);
                builder.get();
                Request req = builder.build();
                Call call = okHttpClient.newCall(req);

                try {

                    Response response = call.execute();

                    ResponseBody body = response.body();

                    if (response.code() != 200) {
                        throw new IllegalAccessException("mreote invoke 失败" + body.string());
                    }
                    File file = new File(fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(fileName);

                    BufferedOutputStream opts = new BufferedOutputStream(fos);

                    IOUtils.copy(body.byteStream(), opts);

                    opts.close();
                    System.out.println(fileIndex + "done");

                    needRedo = false;

                    if (redoTimes > 3) {
                        System.err.println("获取文件失败：" + fileUrl);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    needRedo = true;
                }

            }
        } finally {
            if (countDownLatch != null) {
                countDownLatch.countDown();
            }
        }

    }

    @Override
    public void run() {
        try {
            this.doDownload();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

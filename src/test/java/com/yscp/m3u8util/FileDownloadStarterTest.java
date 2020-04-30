package com.yscp.m3u8util;


import org.junit.Test;

import java.io.IOException;

/**
 * @author shuai-ys
 * @date 2020/4/30
 */
public class FileDownloadStarterTest {


    @Test
    public void main() throws IOException, InterruptedException {
        FileDownloadStarter.doBegins("http://baidu.com/aaa.m3u8");
//        FileDownloadStarter.es.shutdown();
        System.exit(0);
    }
}

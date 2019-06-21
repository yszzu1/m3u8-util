package com.yscp.m3u8util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@SpringBootApplication
public class WebMainApplication {


    public static void main(String[] args) {
        SpringApplication.run(WebMainApplication.class, args);
    }


    @GetMapping("/download")
    public String downLoad(@RequestParam String durl){
        try {
            FileDownloadStarter.doBegins(durl);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "end success";
    }

}

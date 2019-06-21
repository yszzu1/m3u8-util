package com.yscp.m3u8util;

import com.yscp.m3u8util.helper.HttpClientFactory;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class M3u8UrlHandler {


    public List<String> parse2FileList(String url, String fileName) throws IOException {
        Request.Builder builder = new Request.Builder().url(url + "/" + fileName);
        Response res = HttpClientFactory.getInstance().newCall(builder.build()).execute();

        String resStri = new String(res.body().bytes());
        String[] strings = resStri.split("\n");

        List<String> resList = new ArrayList<>();
        for (String string : strings) {
            if (string.endsWith(".ts")) {
                resList.add(url +"/"+ string);
            }
        }
        return resList;
    }

}

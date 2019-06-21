package com.yscp.m3u8util.helper;

public class NumberUtil {


    public static String extent2String(Integer integer, int length){
        StringBuilder sb = new StringBuilder(integer.toString());
        int iLeng = sb.length();

        if(iLeng < length) {

            for (int i = 0; i < length - iLeng; i++) {
                sb.insert(0, "0");
            }

        }
        return sb.toString();
    }


    public static void main(String[] args) {
        String s = NumberUtil.extent2String(2000, 6);
        System.out.println(s);
    }
}

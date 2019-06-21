package com.yscp.m3u8util.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static java.lang.System.out;

public class FileUtil {

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir
                        (new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        if(dir.delete()) {
            return true;
        } else {
            System.out.println("目录删除失败！");
            return false;
        }
    }

    public static final int BUFSIZE = 1024 * 8;


    public static void mergeFiles(String outFile, List<File> files) {
        FileChannel outChannel = null;
        out.println("Merge " + files + " into " + outFile);
        try {
            outChannel = new FileOutputStream(outFile).getChannel();
            for (File f : files) {
                Charset charset = Charset.forName("utf-8");
                CharsetDecoder chdecoder = charset.newDecoder();
                CharsetEncoder chencoder = charset.newEncoder();
                FileChannel fc = new FileInputStream(f).getChannel();
                ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
                CharBuffer charBuffer = chdecoder.decode(bb);
                ByteBuffer nbuBuffer = chencoder.encode(charBuffer);
                while (fc.read(nbuBuffer) != -1) {
                    bb.flip();
                    nbuBuffer.flip();
                    outChannel.write(nbuBuffer);
                    bb.clear();
                    nbuBuffer.clear();
                }
                fc.close();
                out.println(f.getName()+"done");
            }
            out.println("Merged!! ");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
            } catch (IOException ignore) {
                ignore.printStackTrace();
            }
        }
    }


    public static void mergeFiles(String outDirFile, String sourceDir) {
        File[] files = (new File(sourceDir)).listFiles();

        List<File> fileList = Arrays.asList(files);
        // 要求文件名按数字顺序
        fileList.sort(Comparator.comparing(File::getName));
        mergeFiles(outDirFile, fileList);
    }

    public static void main(String[] args) {
        String sourceDir = "D:/tmp/";
        mergeFiles("D:/aaa.ts", sourceDir);
    }
}

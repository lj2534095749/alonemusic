package com.example.util;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static File[] listMusicFiles(File file){
        File[] musicFiles = new File[]{};
        List<File> fileList = new ArrayList<>();
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f : files){
                if(f.length() >= 1024){
                    fileList.add(f);
                }
            }
        }
        return fileList.toArray(musicFiles);
    }

    public static List<String> listMusicFileNameList(File file){
        List<String> fileNameList = new ArrayList<>();
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f : files){
                if(f.length() >= 1024){
                    fileNameList.add(f.getName());
                }
            }
        }
        return fileNameList;
    }

    /*
     * Java文件操作 获取不带扩展名的文件名
     * */
    public static String getFileNameNoExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

}

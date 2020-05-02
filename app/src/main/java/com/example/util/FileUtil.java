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
            for(int i = 0; i < files.length; i++){
                if(files[i].length() >= 1024){
                    fileList.add(files[i]);
                }
            }
        }
        return fileList.toArray(musicFiles);
    }

    public static List<String> listMusicFileName(File file){
        List<String> fileNameList = new ArrayList<>();
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(file != null){
                for(File f : files){
                    if(f.length() >= 1024){
                        fileNameList.add(getFileNameNoExtensionName(f.getName()));
                    }
                }
            }
        }
        return fileNameList;
    }

    public static ArrayList<String> listMusicFilePath(File file){
        ArrayList<String> musicFilePathList = new ArrayList<>();
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(file != null){
                for(File f : files){
                    if(f.length() >= 1024){
                        musicFilePathList.add(f.getPath());
                    }
                }
            }
        }
        return musicFilePathList;
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

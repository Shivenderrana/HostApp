package com.sonu.hostelmanagementsystem;

import android.content.Context;
import android.os.Environment;

import java.io.File;

class FileUtils {

    public static String getAppDir(Context context){
        return context.getExternalFilesDir(null) + "/" + context.getString(R.string.app_name);
    }

    static String getExternalDir(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    static File createDirIfNotExist(String path){
        File dir = new File(path);
        if( !dir.exists() ){
            dir.mkdir();
        }
        return dir;
    }

    static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}
package com.pmjyzy.android.frame.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class DataCleanUtil {
	/**
	 * 清除/data/data/com.xxx.xxx/files下的内容
	 * 
	 * @param context
	 */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
	private static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}

	/** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(Environment.getExternalStorageDirectory()+"/"+filePath));
	}
	
	
	 /** 
     * 删除SD卡或者手机的缓存图片和目录 
     */  
    public static void deleteFile(String fileName) {  
        File dirFile = new File(Environment.getExternalStorageDirectory().getPath()+"/"+fileName);  
       if(! dirFile.exists()){  
           return;  
        }  
        if (dirFile.isDirectory()) {  
            String[] children = dirFile.list();  
            for (int i = 0; i < children.length; i++) {  
                new File(dirFile, children[i]).delete();  
            }  
        }  
        Log.i("result","=============删除成功===========");
        dirFile.delete();  
   }
	
}

package com.pmjyzy.android.frame.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

/**
 * SD卡相关的辅助类
 * 
 * @author zhy
 * 
 */
public class SDCardUtils {
	private SDCardUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断SDCard是否可用
	 * 
	 * @return
	 */
	public static boolean isSDCardEnable() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ File.separator;
	}

	/**
	 * 获取系统存储路径
	 * 
	 * @return
	 */
	public static String getRootDirectoryPath() {
		return Environment.getRootDirectory().getAbsolutePath();
	}

	/**
	 * 获得SD卡总大小 待完善
	 * 
	 * @return
	 */

	
	/**
	 * 获得sd卡剩余容量，即可用大小 待完善
	 * 
	 * @return
	 */

	
	
	
	/**
	 * 获得机身内存总大小
	 * 
	 * @return
	 */
	public static String getRomTotalSize(Context context) {
		if (isSDCardEnable()) {
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long totalBlocks = stat.getBlockCount();
			return Formatter.formatFileSize(context, blockSize * totalBlocks);
		} else {
			return "";
		}

	}

	/**
	 * 获得机身可用内存
	 * 
	 * @return
	 */
	public static String getRomAvailableSize(Context context) {
		if (isSDCardEnable()) {
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();
			return Formatter.formatFileSize(context, blockSize
					* availableBlocks);
		} else {
			return "";
		}
	}
}

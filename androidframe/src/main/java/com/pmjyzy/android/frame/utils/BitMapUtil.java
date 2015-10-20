package com.pmjyzy.android.frame.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BitMapUtil {

	
	public static Bitmap getBitmap(File pePicFile) {

		BitmapFactory.Options bfOptions = new BitmapFactory.Options();

		bfOptions.inDither = false;

		bfOptions.inPurgeable = true;

		bfOptions.inTempStorage = new byte[12 * 1024];

		// bfOptions.inJustDecodeBounds = true;

		File file = new File(pePicFile.getAbsolutePath());
		Log.i("result",
				"pePicFile.getAbsolutePath()==" + pePicFile.getAbsolutePath());
		FileInputStream fs = null;

		try {

			fs = new FileInputStream(file);

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		}

		Bitmap bmp = null;

		if (fs != null)

			try {

				bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null,
						bfOptions);
				return bmp;
			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				if (fs != null) {

					try {

						fs.close();

					} catch (IOException e) {

						e.printStackTrace();

					}

				}

			}

		return null;
	}

	
}

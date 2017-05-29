package com.mahkota_company.android.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;

public class FileUtils {
	private Bitmap bitmapImage;

	public FileUtils(Bitmap bitmapImage) {
		this.bitmapImage = bitmapImage;
	}

	public Bitmap getBitmapImage() {
		return bitmapImage;
	}

	public void setBitmapImage(Bitmap bitmapImage) {
		this.bitmapImage = bitmapImage;
	}

	public static void close(InputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void close(OutputStream stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

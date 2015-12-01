package com.zoway.parkmanage.utils;

import java.io.File;

import android.content.pm.PackageManager;
import android.os.Environment;

public class PathUtils {

	private static String roots = "";
	private static String s = "";

	static {
		boolean flg = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (flg) {
			roots = Environment.getExternalStorageDirectory().toString();
			s = Environment.getExternalStorageDirectory().toString()
					+ File.separator + "parkmanage";
			File f = new File(s);
			if (!f.exists()) {
				f.mkdir();
			}
		}
	};

	public static String getSdPath() {
		return s;
	}

	public static String getTmpImagePath() {
		String ss = null;
		if (s != null) {
			ss = s + File.separator + "tmp" + File.separator;
			File f = new File(ss);
			if (!f.exists()) {
				f.mkdir();
			}
		}
		return ss;
	}

	public static String getWintoneImagePath() {
		String ss = null;
		if (s != null) {
			ss = roots + File.separator + "wintoneimage" + File.separator;
			File f = new File(ss);
			if (!f.exists()) {
				f.mkdir();
			}
		}
		return ss;
	}

}

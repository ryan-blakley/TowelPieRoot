package com.geohot.towelroot;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.util.Log;

/**
 * Thanks to Hashcode for the asset unpack, I think I pulled this from his safestrap app, can't remember if I edited it any
 */
public class Assets {

	public String apkPath = "";
	public String mAppRoot = "";
	final static String APK_FILTER = "assets";

	public void unpackAsset(String filename) {
		try {
			File mApk = new File(apkPath);
			long apkLastModified = mApk.lastModified();
			ZipFile zip = new ZipFile(apkPath);
			Vector<ZipEntry> files = getAssets(zip);
			int apkFilterLength = APK_FILTER.length();
	           
			Enumeration<?> entries = files.elements();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String path = entry.getName().substring(apkFilterLength);
				if (filename.equals(path)) {
					File outputFile = new File(mAppRoot, path);
					outputFile.getParentFile().mkdirs();

					if (outputFile.exists() && entry.getSize() == outputFile.length() && apkLastModified < outputFile.lastModified()) continue;
					FileOutputStream fos = new FileOutputStream(outputFile);
					copyStreams(zip.getInputStream(entry), fos);
					Runtime.getRuntime().exec("chmod 755 " + outputFile.getAbsolutePath());
				}
			}
		} catch (IOException e) {
			Log.e(Var.LOGTAG, "Error: " + e.getMessage());
		}
	}

	public static final int BUFSIZE = 5192;

	public void copyStreams(InputStream is, FileOutputStream fos) {
		BufferedOutputStream os = null;
		try {
			byte data[] = new byte[BUFSIZE];
			int count;
			os = new BufferedOutputStream(fos, BUFSIZE);
			while ((count = is.read(data, 0, BUFSIZE)) != -1) {
				os.write(data, 0, count);
			}
			os.flush();
		} catch (IOException e) {
			Log.e(Var.LOGTAG, "Exception while copying: " + e);
		} finally {
			try {
				if (os != null) {
					os.close();
				}
	        } catch (IOException e2) {
	            Log.e(Var.LOGTAG, "Exception while closing the stream: " + e2);
	        }
	    }
	}

	public Vector<ZipEntry> getAssets(ZipFile apk) {
		Vector<ZipEntry> list = new Vector<ZipEntry>();
		Enumeration<?> entries = apk.entries();
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			if (entry.getName().startsWith(APK_FILTER)) {
	            list.add(entry);
	        }
	    }
	    return list;
	}
}

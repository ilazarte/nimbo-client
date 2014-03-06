package com.blm.nimboclient;

import java.io.File;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;

public class MediaUtils {

	private static final Pattern thumbpattern = Pattern.compile("\\d+x\\d+$");
	
	/**
	 * Get the starting dir.
	 * @return
	 */
	public File getSdcardDir() {
		
		String state = Environment.getExternalStorageState();
		
		if(state.contentEquals(Environment.MEDIA_MOUNTED) || state.contentEquals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			
		    File home = Environment.getExternalStorageDirectory();
		    File dcim = new File(home, Constants.DIRECTORY_DCIM);
		    File camera = new File(dcim, Constants.DIRECTORY_CAMERA);
		    return camera;
		}
		
		throw new RuntimeException("No valid sdcard appears to be available on this device");
	}
	
	/**
	 * Return a mimetype from the available values.
	 * @param contentResolver
	 * @param fileUri (file:/sdcard...)
	 * @return
	 */
	public String getMimeType(ContentResolver contentResolver, String fileUri) {

		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(fileUri);
		MimeTypeMap mime = MimeTypeMap.getSingleton();

		if (extension != null) {
			type = mime.getMimeTypeFromExtension(extension);
			if (type != null) {
				return type;
			}
		}

		Uri uri = Uri.fromFile(new File(fileUri));
		type = contentResolver.getType(uri);
		if (type != null) {
			return type;
		}

		type = URLConnection.guessContentTypeFromName(fileUri);
		
		return type;
	}
	
	/**
	 * Is this uri a thumbnail?
	 * @param uri
	 * @return
	 */
	public boolean isThumbnail(String uri) {
		Matcher matcher = thumbpattern.matcher(uri);
		Boolean res = matcher.find();
		//Log.i(this.getClass().getSimpleName(), "uri and val:" + uri + ", " + res);
		return res;
	}
}

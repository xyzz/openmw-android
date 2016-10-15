package file.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class CopyFilesFromAssets {

	private Context context;
	private String configsPath;
	public CopyFilesFromAssets(Context context,String configsPath) {
		this.context=context;
		this.configsPath=configsPath;
	}
	public void copyFileOrDir(String path) {

		AssetManager assetManager = context.getAssets();
		String assets[] = null;
		try {
			assets = assetManager.list(path);
			if (assets.length == 0) {
				copyFile(path);
			} else {
				String fullPath = configsPath;
				File dir = new File(fullPath);
				if (!dir.exists())
					dir.mkdirs();
				for (int i = 0; i < assets.length; ++i) {
					copyFileOrDir(path + "/" + assets[i]);
				}
			}
		} catch (IOException ex) {

		}

	}

	private void copyFile(String filename) {

		AssetManager assetManager = context.getAssets();

		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(filename);
			filename = filename.replace("libopenmw", "");
			String newFileName = configsPath + filename;
			File tmp = new File(newFileName);
			String dirPath = newFileName.replace(tmp.getName(), "");
			File dir = new File(dirPath);
			if (!dir.exists())
				dir.mkdirs();
			out = new FileOutputStream(newFileName);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {

		}

	}

}

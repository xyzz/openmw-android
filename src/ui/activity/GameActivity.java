package ui.activity;

import java.io.File;

import org.libsdl.app.SDLActivity;

import ui.controls.ScreenControls;
import android.os.Bundle;
import android.os.Process;

public class GameActivity extends SDLActivity {

	public static native void getPathToJni(String path);

	static {

		System.loadLibrary("SDL2");
		System.loadLibrary("openal");
		System.loadLibrary("openmw");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getPathToJni(MainActivity.configsPath);
		deleteVideoFile();
		ScreenControls controls = new ScreenControls(this);
		controls.showControls(MainActivity.contols);

	}

	private void deleteVideoFile() {
		File inputfile = new File(MainActivity.dataPath
				+ "/Video/bethesda logo.bik");
		if (inputfile.exists())
			inputfile.delete();

	}

	@Override
	public void onDestroy() {
		finish();
		Process.killProcess(Process.myPid());

		super.onDestroy();
	}

}

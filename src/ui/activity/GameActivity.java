package ui.activity;

import java.io.File;

import org.libsdl.app.SDLActivity;

import ui.controls.QuickPanel;
import ui.controls.ScreenControls;
import android.os.Bundle;
import android.os.Process;
import android.view.WindowManager;

public class GameActivity extends SDLActivity {

	public static native void getPathToJni(String path);

	private int argc = 0;
	private char[] argv;
	static {

		System.loadLibrary("SDL2");
		System.loadLibrary("openal");
		System.loadLibrary("openmw");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		parseCommandLine();
		getPathToJni(MainActivity.configsPath);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		deleteVideoFile();
		ScreenControls controls = new ScreenControls(this);
		controls.showControls(MainActivity.contols);
		QuickPanel panel = new QuickPanel(this);
		panel.showQuickPanel(MainActivity.contols);

	}

	private void deleteVideoFile() {
		File inputfile = new File(MainActivity.dataPath
				+ "/Video/bethesda logo.bik");
		if (inputfile.exists())
			inputfile.delete();

	}

	private void parseCommandLine() {
		String[] argvData = MainActivity.commandLineData.split(" --");
		argc = argvData.length;
		argv = new char[argc];
		for (int i = 0; i < argc; i++)
			argv[i] = argvData[i].charAt(0);
	}

	@Override
	public void onDestroy() {
		finish();
		Process.killProcess(Process.myPid());
		super.onDestroy();
	}

}

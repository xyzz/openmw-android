package ui.activity;

import java.io.File;

import org.libsdl.app.SDLActivity;

import screen.ScreenScaler;
import ui.controls.QuickPanel;
import ui.controls.ScreenControls;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.WindowManager;

public class GameActivity extends SDLActivity {

	public static native void getPathToJni(String path);

	public static native void commandLine(int argc, String[] argv);

	private int argc = 0;
	private String[] argv;
	static {

		System.loadLibrary("SDL2");
		System.loadLibrary("openal");
		System.loadLibrary("openmw");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		parseCommandLine();
		commandLine(argc, argv);
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
		MainActivity.commandLineData = MainActivity.commandLineData.replace(
				" ", "");
		argv = MainActivity.commandLineData.split("/");
		argc = argv.length;
	}

	@Override
	public void onDestroy() {
		finish();
		Process.killProcess(Process.myPid());
		super.onDestroy();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		ScreenScaler.buttonTextScaler(QuickPanel.getInstance().showPanel, 4);
		ScreenScaler.buttonTextScaler(QuickPanel.getInstance().f1, 4);
		ScreenScaler.buttonTextScaler(ScreenControls.getInstance().buttonTouch, 4);

	}

}

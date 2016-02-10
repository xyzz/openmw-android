
package ui.activity;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.Button;

import com.libopenmw.openmw.R;

import org.libsdl.app.SDLActivity;

import constants.Constants;
import screen.ScreenScaler;
import ui.controls.Joystick;
import ui.controls.QuickPanel;
import ui.controls.ScreenControls;
import ui.controls.TouchCameraSimulation;
import ui.files.CommandlineParser;
import ui.files.ConfigsFileStorageHelper;

public class GameActivity extends SDLActivity {

    public static native void getPathToJni(String path);

    public static native void commandLine(int argc, String[] argv);

    private TouchCameraSimulation touchCamera;

    private boolean hideControls = false;
    private ScreenControls screenControls;
    private static GameActivity Instance = null;


    static {
        System.loadLibrary("avcodec");
        System.loadLibrary("avdevice");
        System.loadLibrary("avfilter");
        System.loadLibrary("avformat");
        System.loadLibrary("avutil");
        System.loadLibrary("swresample");
        System.loadLibrary("swscale");
        System.loadLibrary("openal");
        System.loadLibrary("SDL2");
        System.loadLibrary("openmw");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instance = this;
        KeepScreenOn();
        Joystick.isGameEnabled = true;
        CommandlineParser commandlineParser = new CommandlineParser(Constants.commandLineData);
        commandlineParser.parseCommandLine();
        commandLine(commandlineParser.getArgc(), commandlineParser.getArgv());
        hideControls = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.HIDE_CONTROLS, false);
        getPathToJni(ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH);
        screenControls = new ScreenControls(this);
        screenControls.showControls(hideControls);
        QuickPanel panel = new QuickPanel(this);
        panel.showQuickPanel(hideControls);
        if (!hideControls)
            QuickPanel.getInstance().f1.setVisibility(Button.VISIBLE);
        touchCamera = (TouchCameraSimulation) findViewById(R.id.superTouch);
    }

    public static GameActivity getInstance() {
        return Instance;
    }

    public void hideTouchCamera(boolean needHideCamera) {
        if (!hideControls) {
            touchCamera.hideCamera(needHideCamera);
        }
    }

    private void KeepScreenOn() {
        boolean needKeepScreenOn = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("screen_keeper", false);
        if (needKeepScreenOn) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onDestroy() {
        finish();
        Process.killProcess(Process.myPid());
        super.onDestroy();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hideControls) {
            ScreenScaler.textScaler(QuickPanel.getInstance().showPanel, 4);
            ScreenScaler.textScaler(QuickPanel.getInstance().f1, 4);
            QuickPanel.getInstance().f1.setVisibility(Button.GONE);
            ScreenScaler.textScaler(screenControls.buttonTouch, 4);
        }
    }

}

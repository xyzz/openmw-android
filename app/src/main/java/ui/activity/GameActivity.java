package ui.activity;

import java.io.File;

import org.libsdl.app.SDLActivity;

import constants.Constants;
import screen.ScreenScaler;
import ui.controls.QuickPanel;
import ui.controls.ScreenControls;
import ui.files.PreferencesHelper;

import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class GameActivity extends SDLActivity {

    public static native void getPathToJni(String path);

    public static native void commandLine(int argc, String[] argv);

    private boolean hideControls = false;


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
        hideControls = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.HIDE_CONTROLS, false);
        getPathToJni(Constants.configsPath);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        deleteVideoFile();
        ScreenControls controls = new ScreenControls(this);
        controls.showControls(hideControls);
        QuickPanel panel = new QuickPanel(this);
        panel.showQuickPanel(hideControls);
        if (!hideControls)
            QuickPanel.getInstance().f1.setVisibility(Button.VISIBLE);

    }

    private void deleteVideoFile() {
        File inputfile = new File(Constants.dataPath
                + "/Video/bethesda logo.bik");
        if (inputfile.exists())
            inputfile.delete();

    }

    private void parseCommandLine() {
        Constants.commandLineData = Constants.commandLineData.replace(
                " ", "");
        argv = Constants.commandLineData.split("/");
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
        if (!hideControls) {
            ScreenScaler.textScaler(QuickPanel.getInstance().showPanel, 4);
            ScreenScaler.textScaler(QuickPanel.getInstance().f1, 4);
            QuickPanel.getInstance().f1.setVisibility(Button.GONE);
            ScreenScaler.textScaler(ScreenControls.getInstance().buttonTouch, 4);
        }
    }

}


package ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.libopenmw.openmw.R;

import org.libsdl.app.SDLActivity;

import constants.Constants;
import cursor.MouseCursor;
import parser.CommandlineParser;
import ui.game.GameState;
import ui.screen.ScreenScaler;
import ui.controls.QuickPanel;
import ui.controls.ScreenControls;
import file.ConfigsFileStorageHelper;

import static utils.Utils.hideAndroidControls;

public class GameActivity extends SDLActivity {

    public static native void getPathToJni(String path);

    public static native void commandLine(int argc, String[] argv);

    private FrameLayout controlsRootLayout;
    private boolean hideControls = false;
    private ScreenControls screenControls;
    private MouseCursor cursor;

    @Override
    public void loadLibraries() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String graphicsLibrary = prefs.getString("pref_graphicsLibrary", "");

        System.loadLibrary("c++_shared");
        System.loadLibrary("openal");
        System.loadLibrary("SDL2");
        if (graphicsLibrary.equals("gles2")) {
            try {
                Os.setenv("OPENMW_GLES_VERSION", "2", true);
                Os.setenv("LIBGL_ES", "2", true);
            } catch (ErrnoException e) {
                Log.e("OpenMW", "Failed setting environment variables.");
                e.printStackTrace();
            }
        }
        System.loadLibrary("GL");
        System.loadLibrary("openmw");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameState.setGameState(true);
//        NativeListener.initJavaVm();
        KeepScreenOn();
        parseCommandLineData();
        getPathToJni(ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH);
        showControls();
     }

    private void parseCommandLineData() {
        String cmd = PreferenceManager.getDefaultSharedPreferences(this).getString("commandLine", "");
        CommandlineParser commandlineParser = new CommandlineParser(cmd);
        commandLine(commandlineParser.getArgc(), commandlineParser.getArgv());
    }

    private void showControls() {
        hideControls = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.HIDE_CONTROLS, false);
        if (!hideControls) {
            screenControls = new ScreenControls(this);
            screenControls.showControls(hideControls);
            QuickPanel panel = new QuickPanel(this);
            panel.showQuickPanel(hideControls);
            QuickPanel.getInstance().f1.setVisibility(Button.VISIBLE);
            controlsRootLayout = (FrameLayout) findViewById(R.id.rootLayout);
        }
        cursor = new MouseCursor(this);
    }

    public void hideControlsRootLayout(final boolean needHideControls) {
        if (!hideControls) {
            GameActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (needHideControls) {
                        controlsRootLayout.setVisibility(View.GONE);
                    } else {
                        controlsRootLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
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
        if (hasFocus) {
            hideAndroidControls(this);
        }

        if (!hideControls) {
            ScreenScaler.textScaler(QuickPanel.getInstance().showPanel, 4);
            ScreenScaler.textScaler(QuickPanel.getInstance().f1, 4);
            QuickPanel.getInstance().f1.setVisibility(Button.GONE);
        }
    }

}

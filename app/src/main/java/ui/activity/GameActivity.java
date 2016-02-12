
package ui.activity;

import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.libopenmw.openmw.R;

import org.libsdl.app.SDLActivity;

import constants.Constants;
import listener.NativeListener;
import screen.ScreenScaler;
import ui.controls.Joystick;
import ui.controls.QuickPanel;
import ui.controls.ScreenControls;
import ui.files.CommandlineParser;
import ui.files.ConfigsFileStorageHelper;

public class GameActivity extends SDLActivity {

    public static native void getPathToJni(String path);

    public static native void commandLine(int argc, String[] argv);

    private FrameLayout controlsRootLayout;
    private boolean hideControls = false;
    private ScreenControls screenControls;
    private static GameActivity Instance = null;
    private boolean needHideControls = false;
    private boolean cursorVisible = false;


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
        System.loadLibrary("cursorvisibilty");
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
        if (!hideControls) {
            screenControls = new ScreenControls(this);
            screenControls.showControls(hideControls);
            QuickPanel panel = new QuickPanel(this);
            panel.showQuickPanel(hideControls);
            QuickPanel.getInstance().f1.setVisibility(Button.VISIBLE);
            controlsRootLayout = (FrameLayout) findViewById(R.id.rootLayout);
            backgroundTask();
        }
    }

    public static GameActivity getInstance() {
        return Instance;
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

    private void backgroundTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                }
                while (true) {
                    boolean currentCursorState = NativeListener.getCursorVisible();
                    if (cursorVisible != currentCursorState) {
                        cursorVisible = currentCursorState;
                        hideControlsRootLayout(cursorVisible);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                }

            }
        }).start();

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
        }
    }

}

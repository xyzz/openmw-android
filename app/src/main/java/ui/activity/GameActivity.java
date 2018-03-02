
package ui.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.system.ErrnoException;
import android.system.Os;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.libopenmw.openmw.R;

import org.libsdl.app.SDLActivity;
import org.libsdl.app.SDLInputConnection;

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

    private int numPointersDown;
    private int maxPointersDown;
    private int mouseDeadzone;
    private float startX;
    private float startY;
    private boolean isMoving;
    private double mouseScalingFactor;

    public static native void getPathToJni(String path);

    public static native void commandLine(int argc, String[] argv);

    private FrameLayout controlsRootLayout;
    private boolean hideControls = false;
    private ScreenControls screenControls;
    private MouseCursor cursor;
    private SharedPreferences prefs;

    @Override
    public void loadLibraries() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
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

    public RelativeLayout getLayout() {
        return (RelativeLayout) mLayout;
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

        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        int minRes = Math.min(dm.widthPixels, dm.heightPixels);
        mouseDeadzone = minRes / 40; // fairly arbitrary

        try {
            mouseScalingFactor = Float.parseFloat(prefs.getString("pref_touchpadSensitivity", "1.8"));
        } catch (NumberFormatException e) {
            mouseScalingFactor = 1.8;
        }
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


    // Touch events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (numPointersDown == 0) {
                    startX = event.getX();
                    startY = event.getY();
                }
                ++numPointersDown;
                maxPointersDown = Math.max(numPointersDown, maxPointersDown);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                numPointersDown = Math.max(0, numPointersDown - 1);
                if (numPointersDown == 0) {
                    // everything's up, do the action
                    if (maxPointersDown == 3) {
                        showVirtualInput();
                    } else if (!isMoving && SDLActivity.isMouseShown() != 0) {
                        // only send clicks if we didn't move
                        int mouseX = SDLActivity.getMouseX();
                        int mouseY = SDLActivity.getMouseY();
                        int mouseButton = 0;

                        if (maxPointersDown == 1)
                            mouseButton = 1;
                        else if (maxPointersDown == 2)
                            mouseButton = 2;

                        if (mouseButton != 0) {
                            SDLActivity.onNativeMouse(mouseButton, MotionEvent.ACTION_DOWN, mouseX, mouseY);
                            final Handler handler = new Handler();
                            handler.postDelayed(() -> SDLActivity.onNativeMouse(0, MotionEvent.ACTION_UP, mouseX, mouseY), 100);
                        }
                    }

                    maxPointersDown = 0;
                    isMoving = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (maxPointersDown == 1) {
                    float diffX = event.getX() - startX;
                    float diffY = event.getY() - startY;
                    double distance = Math.sqrt(diffX * diffX + diffY * diffY);

                    if (distance > mouseDeadzone) {
                        isMoving = true;
                        startX = event.getX();
                        startY = event.getY();
                    } else if (isMoving) {
                        int mouseX = SDLActivity.getMouseX();
                        int mouseY = SDLActivity.getMouseY();

                        long newMouseX = Math.round(mouseX + diffX * mouseScalingFactor);
                        long newMouseY = Math.round(mouseY + diffY * mouseScalingFactor);

                        if (SDLActivity.isMouseShown() != 0)
                            SDLActivity.onNativeMouse(0, MotionEvent.ACTION_MOVE, newMouseX, newMouseY);

                        startX = event.getX();
                        startY = event.getY();
                    }
                }
                break;
        }

        return true;
    }

    private void showVirtualInput() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Virtual input");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String text = input.getText().toString();
            SDLInputConnection.nativeCommitText(text, 0);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

}

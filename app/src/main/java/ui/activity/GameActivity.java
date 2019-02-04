
package ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.system.ErrnoException;
import android.system.Os;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.libsdl.app.SDLActivity;

import constants.Constants;
import cursor.MouseCursor;
import parser.CommandlineParser;
import ui.controls.Osc;
import ui.game.GameState;
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

    private boolean hideControls = false;
    private MouseCursor cursor;
    private SharedPreferences prefs;

    String getOpenmwLibName() {
        return "openmw_osg_" + prefs.getString("pref_osg", "");
    }

    @Override
    public void loadLibraries() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String graphicsLibrary = prefs.getString("pref_graphicsLibrary", "");
        String physicsFPS = prefs.getString("pref_physicsFPS", "");
        if (!physicsFPS.isEmpty()) {
            try {
                Os.setenv("OPENMW_PHYSICS_FPS", physicsFPS, true);
                Os.setenv("OSG_TEXT_SHADER_TECHNIQUE", "NO_TEXT_SHADER", true);
            } catch (ErrnoException e) {
                Log.e("OpenMW", "Failed setting environment variables.");
                e.printStackTrace();
            }
        }

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
        System.loadLibrary(getOpenmwLibName());
    }

    protected String getMainSharedObject() {
        return "lib" + getOpenmwLibName() + ".so";
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

    private void showControls() {
        hideControls = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.HIDE_CONTROLS, false);
        if (!hideControls) {
            RelativeLayout layout = getLayout();
            Osc osc = new Osc();
            osc.placeElements(layout);
        }
        cursor = new MouseCursor(this);
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
                    if (!isMoving && SDLActivity.isMouseShown() != 0) {
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

    protected String[] getArguments() {
        String cmd = PreferenceManager.getDefaultSharedPreferences(this).getString("commandLine", "");
        CommandlineParser commandlineParser = new CommandlineParser(cmd);
        return commandlineParser.getArgv();
    }

}

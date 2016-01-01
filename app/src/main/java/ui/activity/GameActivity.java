package ui.activity;

import java.io.File;
import java.util.List;

import org.libsdl.app.SDLActivity;

import constants.Constants;
import screen.ScreenScaler;
import ui.controls.Joystick;
import ui.controls.QuickPanel;
import ui.controls.ScreenControls;
import ui.controls.SdlNativeKeys;
import ui.files.CommandlineParser;

import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Button;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameActivity extends SDLActivity implements SensorEventListener {

    public static native void getPathToJni(String path);

    public static native void commandLine(int argc, String[] argv);

    private float constTouch = 0;
    private float[] xmas = new float[2];
    private float[] ymas = new float[2];

    private SensorManager sManager;
    private boolean useGyroscope = false;
    // public static native void saveCurrentTextureCompressionMode (String textureCompressionMode);

    private boolean hideControls = false;


    static {

        System.loadLibrary("SDL2");
        System.loadLibrary("openal");
        System.loadLibrary("openmw");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KeepScreenOn();
        Joystick.isGameEnabled = true;
        CommandlineParser commandlineParser = new CommandlineParser(Constants.commandLineData);
        commandlineParser.parseCommandLine();
        commandLine(commandlineParser.getArgc(), commandlineParser.getArgv());
        //    saveCurrentTextureCompressionMode(Constants.textureCompressionMode);
        hideControls = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.HIDE_CONTROLS, false);
        getPathToJni(Constants.configsPath);
        ScreenControls controls = new ScreenControls(this);
        controls.showControls(hideControls);
        QuickPanel panel = new QuickPanel(this);
        panel.showQuickPanel(hideControls);
        if (!hideControls)
            QuickPanel.getInstance().f1.setVisibility(Button.VISIBLE);
        sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        useGyroscope = PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Constants.USE_GYROSCOPE, false);

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
    protected void onResume() {
        super.onResume();
        if (useGyroscope)
            sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onStop() {
        if (useGyroscope)
            sManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (useGyroscope) {

            if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
                return;
            }

            simulateCameraMovement(event.values[2], event.values[1]);
        }
    }


    private void simulateCameraMovement(float x, float y) {

        xmas[0] = x;
        ymas[0] = y;

        if (xmas[0] == xmas[1] && ymas[0] < ymas[1]
                && ymas[1] - ymas[0] > constTouch) {

        } else if (xmas[0] == xmas[1] && ymas[0] > ymas[1]
                && ymas[0] - ymas[1] > constTouch)

        {
            SDLActivity.onNativeTouch(0, 0,
                    MotionEvent.ACTION_DOWN, 0.5f, 0.3f, 0);


        } else if (xmas[0] < xmas[1] && ymas[0] == ymas[1]
                && xmas[1] - xmas[0] > constTouch)

        {
            SDLActivity.onNativeTouch(0, 0,
                    MotionEvent.ACTION_DOWN, 0.9f, 0.5f, 0);

        } else if (xmas[0] > xmas[1] && ymas[0] == ymas[1]
                && xmas[0] - xmas[1] > constTouch)

        {
            SDLActivity.onNativeTouch(0, 0,
                    MotionEvent.ACTION_DOWN, 0.3f, 0.5f, 0);


        } else if (xmas[0] < xmas[1] && ymas[0] < ymas[1]
                && ymas[1] - ymas[0] > constTouch
                && xmas[1] - xmas[0] > constTouch)

        {
            SDLActivity.onNativeTouch(0, 0,
                    MotionEvent.ACTION_DOWN, 0.9f, 0.9f, 0);

        } else if (xmas[0] > xmas[1] && ymas[0] > ymas[1]
                && ymas[0] - ymas[1] > constTouch
                && xmas[0] - xmas[1] > constTouch)

        {
            SDLActivity.onNativeTouch(0, 0,
                    MotionEvent.ACTION_DOWN, 0.3f, 0.3f, 0);

        } else if (xmas[0] < xmas[1] && ymas[0] > ymas[1]
                && ymas[0] - ymas[1] > constTouch
                && xmas[1] - xmas[0] > constTouch)

        {
            SDLActivity.onNativeTouch(0, 0,
                    MotionEvent.ACTION_DOWN, 0.9f, 0.3f, 0);

        } else if (xmas[0] > xmas[1] && ymas[0] < ymas[1]
                && ymas[1] - ymas[0] > constTouch
                && xmas[0] - xmas[1] > constTouch)

        {
            SDLActivity.onNativeTouch(0, 0,
                    MotionEvent.ACTION_DOWN, 0.3f, 0.9f, 0);

        }

        xmas[0] = xmas[1];
        ymas[0] = ymas[1];


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

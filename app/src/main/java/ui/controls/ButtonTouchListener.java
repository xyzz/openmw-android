package ui.controls;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;

import org.libsdl.app.SDLActivity;

public class ButtonTouchListener implements OnTouchListener {

    private int keyCode;
    boolean needEmulateMouse = false;

    private enum Movement {
        KEY_DOWN,
        KEY_UP,
        MOUSE_DOWN,
        MOUSE_UP
    }

    public ButtonTouchListener(int keyCode, boolean needEmulateMouse) {
        this.keyCode = keyCode;
        this.needEmulateMouse = needEmulateMouse;
        SDLActivity.mSeparateMouseAndTouch = needEmulateMouse;
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(v);
                return true;
            case MotionEvent.ACTION_UP:
                onTouchUp(v);
                return true;

            case MotionEvent.ACTION_CANCEL:
                onTouchUp(v);
                return true;
        }
        return false;
    }

    private void onTouchDown(View v) {
        ScaleSimulation.onTouchDown(v);
        if (!needEmulateMouse) {
            eventMovement(Movement.KEY_DOWN);
        } else {
            eventMovement(Movement.MOUSE_DOWN);
        }
    }

    private void onTouchUp(View v) {
        ScaleSimulation.onTouchUp(v);
        if (!needEmulateMouse) {
            eventMovement(Movement.KEY_UP);
        } else {
            eventMovement(Movement.MOUSE_UP);
        }
    }

    protected void eventMovement(Movement event) {
        switch (event) {
            case KEY_DOWN:
                SdlNativeKeys.keyDown(keyCode);
                break;
            case KEY_UP:
                SdlNativeKeys.keyUp(keyCode);
                break;
            case MOUSE_DOWN:
                SDLActivity.onNativeMouse(keyCode, MotionEvent.ACTION_DOWN, -20.5f, -20.5f);
                break;
            case MOUSE_UP:
                SDLActivity.onNativeMouse(keyCode, MotionEvent.ACTION_UP, -20.5f, -20.5f);
                break;
        }
    }
}
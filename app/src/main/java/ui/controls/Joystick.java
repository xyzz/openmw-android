package ui.controls;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class Joystick extends View {

    public static boolean isGameEnabled = false;
    private float x1, x2, y1, y2;
    private DirectionListener directionListener;

    public Joystick(Context context) {
        super(context);
        drawBackgroundColor();
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawBackgroundColor();
    }

    public Joystick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        drawBackgroundColor();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (directionListener == null) {
            directionListener = new DirectionListener();
        }
        playerMovement(event);
        return true;
    }

    private void drawBackgroundColor() {
        if (!isGameEnabled) {
            this.setBackgroundColor(Color.GRAY);
        }
    }

    private void playerMovement(MotionEvent event) {
        int actionType = event.getAction();
        switch (actionType) {
            case (MotionEvent.ACTION_DOWN):
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_MOVE: {
                x2 = event.getX();
                y2 = event.getY();
                onJoystickDirection(directionListener.getCurrentDirection(x1, y1, x2, y2));
                x1 = x2;
                y1 = y2;
                break;
            }
            case MotionEvent.ACTION_UP: {
                releaseKeys();
                break;
            }
            default: {
                releaseKeys();
                break;
            }
        }
    }

    private void onJoystickDirection(DirectionListener.Direction currentDirection) {
        releaseKeys();
        switch (currentDirection) {
            case LEFT:
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_A);
                break;
            case RIGHT:
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_D);
                break;
            case UP:
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_W);
                break;
            case DOWN:
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_S);
                break;
            case DOWN_LEFT:
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_A);
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_S);
                break;
            case DOWN_RIGHT:
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_D);
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_S);
                break;
            case UP_LEFT:
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_A);
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_W);
                break;
            case UP_RIGHT:
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_D);
                SdlNativeKeys.keyDown(KeyEvent.KEYCODE_W);
                break;
        }
    }

    private void releaseKeys() {
        SdlNativeKeys.keyUp(KeyEvent.KEYCODE_W);
        SdlNativeKeys.keyUp(KeyEvent.KEYCODE_S);
        SdlNativeKeys.keyUp(KeyEvent.KEYCODE_A);
        SdlNativeKeys.keyUp(KeyEvent.KEYCODE_D);
    }


}
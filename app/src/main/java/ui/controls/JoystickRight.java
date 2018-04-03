package ui.controls;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import org.libsdl.app.SDLActivity;

public class JoystickRight extends Joystick {

    private float curX, curY;

    public JoystickRight(Context context) {
        super(context);
    }

    public JoystickRight(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JoystickRight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                curX = event.getX();
                curY = event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getX();
                float newY = event.getY();

                float mouseScalingFactor = 2.0f; // TODO: make configurable

                float movementX = (newX - curX) * mouseScalingFactor;
                float movementY = (newY - curY) * mouseScalingFactor;

                SDLActivity.sendRelativeMouseMotion(Math.round(movementX), Math.round(movementY));

                curX = newX;
                curY = newY;
                break;
        }

        return super.onTouchEvent(event);
    }
}

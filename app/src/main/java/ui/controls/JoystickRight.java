package ui.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.libsdl.app.SDLActivity;

public class JoystickRight extends Joystick {

    private float startX, startY;

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
            case MotionEvent.ACTION_POINTER_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getX();
                float newY = event.getY();

                float mouseScalingFactor = 2.0f; // TODO: make configurable

                float movementX = (newX - startX) * mouseScalingFactor;
                float movementY = (newY - startY) * mouseScalingFactor;

                SDLActivity.sendRelativeMouseMotion(Math.round(movementX), Math.round(movementY));

                startX = newX;
                startY = newY;
                break;
        }

        return super.onTouchEvent(event);
    }

}

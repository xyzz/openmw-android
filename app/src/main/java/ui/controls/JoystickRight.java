package ui.controls;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;

import org.libsdl.app.SDLActivity;

public class JoystickRight extends Joystick {

    private float startX, startY, curX, curY;

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
                startX = curX = event.getX();
                startY = curY = event.getY();
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
            case MotionEvent.ACTION_UP:
                float diffX = event.getX() - startX;
                float diffY = event.getY() - startY;

                // Simulate a click if finger didn't move much
                double distance = Math.sqrt(diffX * diffX + diffY * diffY);
                if (distance < getWidth() / 50) {
                    SDLActivity.sendMouseButton(1, 1);
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> SDLActivity.sendMouseButton(0, 1), 100);
                }

                break;
        }

        return super.onTouchEvent(event);
    }

}

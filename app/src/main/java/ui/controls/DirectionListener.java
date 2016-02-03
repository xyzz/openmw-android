package ui.controls;


import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class DirectionListener {

    private float constTouch = 0;
    private float measurementErrorMovement = 0f;

    public enum Direction {
        LEFT,
        RIGHT,
        DOWN,
        UP,
        DOWN_RIGHT,
        DOWN_LEFT,
        UP_LEFT,
        UP_RIGHT,
        UNDEFINED;
    }

    private Direction currentDirection;

    public DirectionListener(Context context) {
        currentDirection = Direction.UNDEFINED;
        calculateErrorMovent(context);
    }

    private void calculateErrorMovent(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        constTouch = (float) display.getWidth() / 385.f;
        measurementErrorMovement = constTouch / 2.f;
    }

    public Direction getCurrentDirection(float x1, float y1, float x2, float y2) {
        currentDirection = ComputeDirection(x1, y1, x2, y2);
        return currentDirection;
    }

    private Direction ComputeDirection(float x1, float y1, float x2, float y2) {
        float dy = Math.abs(Math.abs(y1) - Math.abs(y2));
        float dx = Math.abs(Math.abs(x1) - Math.abs(x2));
        boolean isXnotChanges = dx < measurementErrorMovement;
        boolean isYnotChanges = dy < measurementErrorMovement;

        if (isXnotChanges && y1 < y2
                && dy > constTouch) {
            return Direction.DOWN;
        } else if (isXnotChanges && y1 > y2
                && dy > constTouch) {
            return Direction.UP;
        } else if (x1 < x2 && isYnotChanges
                && x2 - x1 > constTouch) {
            return Direction.RIGHT;
        } else if (x1 > x2 && isYnotChanges
                && x1 - x2 > constTouch) {
            return Direction.LEFT;
        } else if (x1 < x2 && y1 < y2
                && y2 - y1 > constTouch
                && x2 - x1 > constTouch) {
            return Direction.DOWN_RIGHT;
        } else if (x1 > x2 && y1 > y2
                && y1 - y2 > constTouch
                && x1 - x2 > constTouch) {
            return Direction.UP_LEFT;
        } else if (x1 < x2 && y1 > y2
                && y1 - y2 > constTouch
                && x2 - x1 > constTouch) {
            return Direction.UP_RIGHT;
        } else if (x1 > x2 && y1 < y2
                && y2 - y1 > constTouch
                && x1 - x2 > constTouch) {
            return Direction.DOWN_LEFT;
        } else
            return currentDirection;
    }

}
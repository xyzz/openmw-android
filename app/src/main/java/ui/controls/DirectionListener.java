package ui.controls;


import android.content.Context;

import screen.ScreenInfo;

public class DirectionListener {

    private float constTouch = 0f;
    private boolean isTouchCameraChosen = false;
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

    public DirectionListener(Context context, boolean isTouchCameraChosen) {
        currentDirection = Direction.UNDEFINED;
        this.isTouchCameraChosen = isTouchCameraChosen;
        calculateErrorMovment(context);
    }

    private void calculateErrorMovment(Context context) {
        ScreenInfo screenInfo = new ScreenInfo(context);
        if (!isTouchCameraChosen) {
            constTouch = screenInfo.screenWidth / 385.f;
            measurementErrorMovement = constTouch / 2.f;
        } else if (screenInfo.diagonalSize() < 7) {
            constTouch = screenInfo.screenHeight / screenInfo.screenWidth;
        }
    }

    public Direction getCurrentDirection(float x1, float y1, float x2, float y2) {
        currentDirection = ComputeDirection(x1, y1, x2, y2);
        return currentDirection;
    }

    private Direction ComputeDirection(float x1, float y1, float x2, float y2) {
        float dy = Math.abs(Math.abs(y1) - Math.abs(y2));
        float dx = Math.abs(Math.abs(x1) - Math.abs(x2));
        boolean isXnotChanges = dx <= measurementErrorMovement;
        boolean isYnotChanges = dy <= measurementErrorMovement;

        if (isXnotChanges && y1 < y2
                && dy > constTouch) {
            return Direction.DOWN;
        } else if (isXnotChanges && y1 > y2
                && dy > constTouch) {
            return Direction.UP;
        } else if (x1 < x2 && isYnotChanges
                && dx > constTouch) {
            return Direction.RIGHT;
        } else if (x1 > x2 && isYnotChanges
                && dx > constTouch) {
            return Direction.LEFT;
        } else if (x1 < x2 && y1 < y2
                && dy > constTouch
                && dx > constTouch) {
            return Direction.DOWN_RIGHT;
        } else if (x1 > x2 && y1 > y2
                && dy > constTouch
                && dx > constTouch) {
            return Direction.UP_LEFT;
        } else if (x1 < x2 && y1 > y2
                && dy > constTouch
                && dx > constTouch) {
            return Direction.UP_RIGHT;
        } else if (x1 > x2 && y1 < y2
                && dy > constTouch
                && dx > constTouch) {
            return Direction.DOWN_LEFT;
        } else {
            if (!isTouchCameraChosen) {
                return currentDirection;
            } else {
                return Direction.UNDEFINED;
            }
        }
    }

}
package ui.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchCameraSimulation extends View {

    private float x1, x2, y1, y2;
    private DirectionListener directionListener;
    private MotionEvent event;
    private Context context;

    public TouchCameraSimulation(Context context) {
        super(context);
        initView();
        this.context = context;
    }

    public TouchCameraSimulation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        this.context = context;
    }

    public TouchCameraSimulation(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        initView();
        this.context = context;
    }

    private void initView() {
        setFocusable(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (directionListener == null) {
            directionListener = new DirectionListener(context,true);
        }
        this.event = event;
        simulateCameraMovement();
        return true;
    }

    private void simulateCameraMovement() {
        int actionType = event.getAction();

        switch (actionType) {
            case MotionEvent.ACTION_DOWN: {
                x1 = event.getX();
                y1 = event.getY();
                SdlNativeKeys.touchDown(0f, 0f, MotionEvent.ACTION_DOWN, event);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                x2 = event.getX();
                y2 = event.getY();
                onCameraMovement(directionListener.getCurrentDirection(x1, y1, x2, y2));
                x1 = x2;
                y1 = y2;
                break;
            }
            case MotionEvent.ACTION_UP: {
                SdlNativeKeys.touchDown(0f, 0f, MotionEvent.ACTION_UP, event);
                break;
            }
            default:
                break;
        }

    }

    private void onCameraMovement(DirectionListener.Direction currentDirection) {
        switch (currentDirection) {
            case LEFT:
                SdlNativeKeys.touchDown(0.3f, 0.5f, MotionEvent.ACTION_MOVE, event);
                break;
            case RIGHT:
                SdlNativeKeys.touchDown(0.9f, 0.5f, MotionEvent.ACTION_MOVE, event);
                break;
            case UP:
                SdlNativeKeys.touchDown(0.5f, 0.3f, MotionEvent.ACTION_MOVE, event);
                break;
            case DOWN:
                SdlNativeKeys.touchDown(0.5f, 0.9f, MotionEvent.ACTION_MOVE, event);
                break;
            case DOWN_LEFT:
                SdlNativeKeys.touchDown(0.3f, 0.9f, MotionEvent.ACTION_MOVE, event);
                break;
            case DOWN_RIGHT:
                SdlNativeKeys.touchDown(0.9f, 0.9f, MotionEvent.ACTION_MOVE, event);
                break;
            case UP_LEFT:
                SdlNativeKeys.touchDown(0.3f, 0.3f, MotionEvent.ACTION_MOVE, event);
                break;
            case UP_RIGHT:
                SdlNativeKeys.touchDown(0.9f, 0.3f, MotionEvent.ACTION_MOVE, event);
                break;
            case UNDEFINED:
                SdlNativeKeys.touchDown(0f, 0f, MotionEvent.ACTION_UP, event);
                break;
        }
    }

}

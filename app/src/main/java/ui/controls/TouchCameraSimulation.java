package ui.controls;

import org.libsdl.app.SDLActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class TouchCameraSimulation extends View {

    private float constTouch = 0;
    private float[] xmas = new float[2];
    private float[] ymas = new float[2];
    private  MotionEvent event;

    public TouchCameraSimulation(Context context) {
        super(context);
        initView();
        cameraTouchConst(context);
    }

    public TouchCameraSimulation(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        cameraTouchConst(context);

    }

    public TouchCameraSimulation(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
        initView();
        cameraTouchConst(context);

    }

    private void initView() {

        setFocusable(true);

    }

    private double tabletSize(Context context) {

        double size = 0;
        try {

            // Compute screen size

            DisplayMetrics dm = context.getResources().getDisplayMetrics();

            float screenWidth = dm.widthPixels / dm.xdpi;

            float screenHeight = dm.heightPixels / dm.ydpi;

            size = Math.sqrt(Math.pow(screenWidth, 2) +

                    Math.pow(screenHeight, 2));

        } catch (Throwable t) {

        }

        return size;

    }

    private void cameraTouchConst(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        if (tabletSize(context) >= 7.0)
            constTouch = 0;
        else
            constTouch = (float) display.getHeight() / display.getWidth();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.event=event;
        simulateCameraMovement();
        return true;
    }


    private void simulateCameraMovement() {
        int actionType = event.getAction();

        switch (actionType) {
            case MotionEvent.ACTION_DOWN: {
                xmas[0] = event.getX();
                ymas[0] = event.getY();
                moveCamera(0f,0f ,MotionEvent.ACTION_DOWN);

                break;
            }
            case MotionEvent.ACTION_MOVE: {
                xmas[1] = event.getRawX();
                ymas[1] = event.getRawY();
                if (xmas[0] == xmas[1] && ymas[0] < ymas[1]
                        && ymas[1] - ymas[0] > constTouch) {
                    moveCamera( 0.5f, 0.9f, MotionEvent.ACTION_MOVE);
                } else if (xmas[0] == xmas[1] && ymas[0] > ymas[1]
                        && ymas[0] - ymas[1] > constTouch)

                {
                    moveCamera( 0.5f, 0.3f,  MotionEvent.ACTION_MOVE);

                } else if (xmas[0] < xmas[1] && ymas[0] == ymas[1]
                        && xmas[1] - xmas[0] > constTouch)

                {
                    moveCamera( 0.9f, 0.5f, MotionEvent.ACTION_MOVE);
                } else if (xmas[0] > xmas[1] && ymas[0] == ymas[1]
                        && xmas[0] - xmas[1] > constTouch)

                {
                    moveCamera( 0.3f, 0.5f, MotionEvent.ACTION_MOVE);

                } else if (xmas[0] < xmas[1] && ymas[0] < ymas[1]
                        && ymas[1] - ymas[0] > constTouch
                        && xmas[1] - xmas[0] > constTouch)

                {
                    moveCamera(0.9f, 0.9f, MotionEvent.ACTION_MOVE);
                } else if (xmas[0] > xmas[1] && ymas[0] > ymas[1]
                        && ymas[0] - ymas[1] > constTouch
                        && xmas[0] - xmas[1] > constTouch)

                {
                    moveCamera(0.3f, 0.3f,  MotionEvent.ACTION_MOVE);
                } else if (xmas[0] < xmas[1] && ymas[0] > ymas[1]
                        && ymas[0] - ymas[1] > constTouch
                        && xmas[1] - xmas[0] > constTouch)

                {
                    moveCamera(0.9f, 0.3f, MotionEvent.ACTION_MOVE);
                } else if (xmas[0] > xmas[1] && ymas[0] < ymas[1]
                        && ymas[1] - ymas[0] > constTouch
                        && xmas[0] - xmas[1] > constTouch)

                {
                    moveCamera(0.3f, 0.9f, MotionEvent.ACTION_MOVE);
                } else
                    moveCamera(0f, 0f, MotionEvent.ACTION_UP);

                xmas[0] = xmas[1];
                ymas[0] = ymas[1];

                break;
            }
            case MotionEvent.ACTION_UP: {
                xmas[0] = xmas[1] = ymas[0] = ymas[1] = 0;
                moveCamera(0f, 0f, MotionEvent.ACTION_UP);

                break;
            }
            default:
                break;
        }


    }


    private void moveCamera(final float x, final float y, final int eventAction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int touchDevId = event.getDeviceId();
                int i = event.getActionIndex();
                int pointerID=event.getPointerId(i);
                float pointerCount =event.getPressure(i);

                SDLActivity.onNativeTouch(touchDevId, pointerID,
                        eventAction, x, y, pointerCount);

            }
        }).start();
    }
}

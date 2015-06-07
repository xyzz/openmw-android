package ui.controls;

import android.view.MotionEvent;

import org.libsdl.app.SDLActivity;

public class SdlNativeKeys {

    public static void keyDown(final int keyCode) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SDLActivity.onNativeKeyDown(keyCode);

            }
        }).start();
    }


    public static void keyUp(final int keyCode) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                SDLActivity.onNativeKeyUp(keyCode);

            }
        }).start();
    }

    public static void touchDown(final float x, final float y, final int eventAction, MotionEvent event) {
        final int touchDevId = event.getDeviceId();
        int i = event.getActionIndex();
        final int pointerID = event.getPointerId(i);
        final float pointerCount = event.getPressure(i);
        new Thread(new Runnable() {
            @Override
            public void run() {
                SDLActivity.onNativeTouch(touchDevId, pointerID,
                        eventAction, x, y, pointerCount);

            }
        }).start();

    }


}

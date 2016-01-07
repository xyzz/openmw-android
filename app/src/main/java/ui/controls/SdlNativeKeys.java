package ui.controls;

import android.view.MotionEvent;

import org.libsdl.app.SDLActivity;

public class SdlNativeKeys {

    public static void keyDown(final int keyCode) {
        SDLActivity.onNativeKeyDown(keyCode);
    }


    public static void keyUp(final int keyCode) {
        SDLActivity.onNativeKeyUp(keyCode);
    }

    public static void touchDown(final float x, final float y, final int eventAction, MotionEvent event) {
        final int touchDevId = event.getDeviceId();
        int i = event.getActionIndex();
        final int pointerID = event.getPointerId(i);
        final float pointerCount = event.getPressure(i);
        SDLActivity.onNativeTouch(touchDevId, pointerID,
                eventAction, x, y, pointerCount);


    }


}

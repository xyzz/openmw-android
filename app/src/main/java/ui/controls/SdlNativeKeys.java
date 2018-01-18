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


}

package org.libsdl.app;

import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by sandstranger on 20.10.15.
 */
public class EscapeKeySimulation {
    private static final int KEYCODE_BACK = 4;

    public static void onBackPressed(int keyCode, KeyEvent event) {
        if (keyCode == KEYCODE_BACK)
            if (event.getAction() == KeyEvent.ACTION_DOWN)
                SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_ESCAPE);
            else if (event.getAction() == KeyEvent.ACTION_UP)
                SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_ESCAPE);
    }
}

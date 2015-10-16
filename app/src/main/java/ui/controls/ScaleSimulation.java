package ui.controls;

import android.view.View;

public class ScaleSimulation {
    private static final float MAX_SCALE = 1.3f;

    public static void onTouchDown(View v) {
        v.setScaleX(MAX_SCALE);
        v.setScaleY(MAX_SCALE);
    }

    public static void onTouchUp(View v) {
        v.setScaleX(1.0f);
        v.setScaleY(1.0f);
    }
}

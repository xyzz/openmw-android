package ui.controls;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;

public class ButtonTouchListener implements OnTouchListener {

    private int keyCode;


    public ButtonTouchListener(int keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ScaleSimulation.onTouchDown(v);
                SdlNativeKeys.keyDown(keyCode);
                return true;
            case MotionEvent.ACTION_UP:
                ScaleSimulation.onTouchUp(v);
                SdlNativeKeys.keyUp(keyCode);
                return true;

            case MotionEvent.ACTION_CANCEL:
                ScaleSimulation.onTouchUp(v);
                SdlNativeKeys.keyUp(keyCode);
                return true;

        }
        return false;
    }

}
package listener;

import ui.activity.GameActivity;

/**
 * Created by sandstranger on 10.02.16.
 */
public class NativeListener {

    public static native void initJavaVm();

    public static void hideTouchCamera(boolean needHideCamera) {
        if (GameActivity.getInstance() != null) {
            GameActivity.getInstance().hideTouchCamera(needHideCamera);
        }
    }
}

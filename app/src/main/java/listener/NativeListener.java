package listener;

import gl.GLDepthBufferHelper;

/**
 * Created by sandstranger on 10.02.16.
 */
public class NativeListener {

    public static int getGlDepthBufferSize() {
        return GLDepthBufferHelper.getGlDepthBufferSize();
    }

    public static native void initJavaVm();

    public static native boolean getCursorVisible();
}

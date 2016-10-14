package cursor;

/**
 * Created by sandstranger on 13.02.16.
 */
public class CursorVisibility {
    private static native void InitBackgroundTask();

    private static native void StopBackgroundTask();

    private ControlsHider controlsHider;
    private static CursorVisibility Instance = null;

    public CursorVisibility(ControlsHider controlsHider) {
        Instance = this;
        this.controlsHider = controlsHider;
    }

    public void runBackgroundTask() {
        InitBackgroundTask();
    }

    //called from c++ code
    public static void updateScreenControlsState(boolean cursorVisibility) {
        if (Instance != null) {
            Instance.controlsHider.hideControlsRootLayout(cursorVisibility);
        }
    }

    public void stopBackgroundTask() {
        StopBackgroundTask();
    }

}
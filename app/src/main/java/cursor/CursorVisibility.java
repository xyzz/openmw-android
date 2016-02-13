package cursor;

import listener.NativeListener;

/**
 * Created by sandstranger on 13.02.16.
 */
public class CursorVisibility {

    private boolean cursorVisible = false;

    private ControlsHider controlsHider;
    private static final int THREAD_SLEEP_TIME = 1000;

    public CursorVisibility(ControlsHider controlsHider) {
        this.controlsHider = controlsHider;
    }

    public void runBackgroundTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    sleepThread();
                    boolean currentCursorState = NativeListener.getCursorVisible();
                    if (cursorVisible != currentCursorState) {
                        cursorVisible = currentCursorState;
                        controlsHider.hideControlsRootLayout(cursorVisible);
                    }
                }

            }
        }).start();

    }

    private void sleepThread() {
        try {
            Thread.sleep(THREAD_SLEEP_TIME);
        } catch (Exception e) {
        }

    }

}

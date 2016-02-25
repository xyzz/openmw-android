package cursor;

import listener.NativeListener;

/**
 * Created by sandstranger on 13.02.16.
 */
public class CursorVisibility {

    private boolean cursorVisible = false;

    private ControlsHider controlsHider;
    private static final int THREAD_SLEEP_TIME = 1000;
    private Thread thread;

    public CursorVisibility(ControlsHider controlsHider) {
        this.controlsHider = controlsHider;
    }

    public void runBackgroundTask() {
     thread= new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    sleepThread();
                    boolean currentCursorState = NativeListener.getCursorVisible();
                    if (cursorVisible != currentCursorState) {
                        cursorVisible = currentCursorState;
                        controlsHider.hideControlsRootLayout(cursorVisible);
                    }
                }

            }
        });
        thread.start();
    }

    public void stopBackgroundTask(){
        if (!thread.isInterrupted()){
            thread.interrupt();
        }
    }
    private void sleepThread() {
        try {
            Thread.sleep(THREAD_SLEEP_TIME);
        } catch (Exception e) {
        }

    }

}
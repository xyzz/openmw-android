package ui.controls;

import org.libsdl.app.SDLActivity;

class GamepadEmulator {

    private static Boolean registered = false;

    static void updateStick(int stickId, float x, float y) {
        int deviceId = 1;

        if (!registered) {
            registered = true;
            SDLActivity.nativeAddJoystick(deviceId, "Virtual", 0, -1,
                    4, 0, 0);
        }

        SDLActivity.onNativeJoy(deviceId, stickId * 2    , x);
        SDLActivity.onNativeJoy(deviceId, stickId * 2 + 1, y);
    }

}

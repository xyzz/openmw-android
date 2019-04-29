package ui.controls

import org.libsdl.app.SDLControllerManager

internal object GamepadEmulator {

    private var registered = false

    fun updateStick(stickId: Int, x: Float, y: Float) {
        // random device ID to make sure it doesn't conflict with anything
        val deviceId = 1384510555

        if (!registered) {
            registered = true
            SDLControllerManager.nativeAddJoystick(deviceId, "Virtual", "Virtual",
                0xbad, 0xf00d,
                false, -0x1,
                4, 0, 0)
        }

        SDLControllerManager.onNativeJoy(deviceId, stickId * 2, x)
        SDLControllerManager.onNativeJoy(deviceId, stickId * 2 + 1, y)
    }

}

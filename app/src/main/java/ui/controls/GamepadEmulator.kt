/*
    Copyright (C) 2018, 2019 Ilya Zhuravlev

    This file is part of OpenMW-Android.

    OpenMW-Android is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    OpenMW-Android is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with OpenMW-Android.  If not, see <https://www.gnu.org/licenses/>.
*/

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

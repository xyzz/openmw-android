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

import android.content.Context
import androidx.core.math.MathUtils
import android.util.AttributeSet

class JoystickLeft : Joystick {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int)
        : super(context, attrs, defStyle)

    override fun updateStick() {
        if (down) {
            // GamepadEmulator takes values on a scale [-1; 1] so convert our values
            val w = (width / 3).toFloat()
            var diffX = currentX - initialX
            var diffY = currentY - initialY

            val bias = 0.3f

            if (Math.abs(diffX) > Math.abs(diffY)) {
                diffY = Math.signum(diffY) * Math.max(0f, Math.abs(diffY) - bias * Math.abs(diffX))
            } else {
                diffX = Math.signum(diffX) * Math.max(0f, Math.abs(diffX) - bias * Math.abs(diffY))
            }

            val dx = MathUtils.clamp(diffX / w + 0.2f * Math.signum(diffX), -1f, 1f)
            val dy = MathUtils.clamp(diffY / w + 0.2f * Math.signum(diffY), -1f, 1f)
            GamepadEmulator.updateStick(stickId, dx, dy)
        } else {
            GamepadEmulator.updateStick(stickId, 0f, 0f)
        }
    }
}

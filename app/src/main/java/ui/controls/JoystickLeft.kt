package ui.controls

import android.content.Context
import androidx.core.math.MathUtils
import android.util.AttributeSet

class JoystickLeft : Joystick {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun updateStick() {
        if (down!!) {
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

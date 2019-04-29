package ui.controls;

import android.content.Context;
import androidx.core.math.MathUtils;
import android.util.AttributeSet;

public class JoystickLeft extends Joystick {

    public JoystickLeft(Context context) {
        super(context);
    }

    public JoystickLeft(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JoystickLeft(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override protected void updateStick() {
        if (down) {
            // GamepadEmulator takes values on a scale [-1; 1] so convert our values
            float w = getWidth() / 3;
            float diffX = currentX - initialX;
            float diffY = currentY - initialY;

            float bias = 0.3f;

            if (Math.abs(diffX) > Math.abs(diffY)) {
                diffY = Math.signum(diffY) * (Math.max(0, Math.abs(diffY) - bias * Math.abs(diffX)));
            } else {
                diffX = Math.signum(diffX) * (Math.max(0, Math.abs(diffX) - bias * Math.abs(diffY)));
            }

            float dx = MathUtils.clamp(diffX / w + 0.2f * Math.signum(diffX), -1, 1);
            float dy = MathUtils.clamp(diffY / w + 0.2f * Math.signum(diffY), -1, 1);
            GamepadEmulator.updateStick(stickId, dx, dy);
        } else {
            GamepadEmulator.updateStick(stickId, 0, 0);
        }
    }
}

package ui.controls;

import android.content.Context;
import android.support.v4.math.MathUtils;
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
            float dx = MathUtils.clamp((currentX - initialX) / w, -1, 1) / 4;
            float dy = MathUtils.clamp((currentY - initialY) / w, -1, 1);
            GamepadEmulator.updateStick(stickId, dx, dy);
        } else {
            GamepadEmulator.updateStick(stickId, 0, 0);
        }
    }
}

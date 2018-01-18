package ui.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.math.MathUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import org.libsdl.app.SDLActivity;

public class Joystick extends View {

    // Stick positions, [-1; 1]
    private float posX, posY;
    // left or right stick
    private int stickId = 0;

    private Paint paint = new Paint();

    public Joystick(Context context) {
        super(context);
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Joystick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setStick(int id) {
        stickId = id;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        canvas.drawRect(0, 0, getWidth() - 1, getHeight() - 1, paint);
        paint.setColor(Color.GREEN);

        float cx = (posX + 1.0f) * getWidth() / 2;
        float cy = (posY + 1.0f) * getHeight() / 2;
        float r = getWidth() / 5;
        canvas.drawCircle(cx, cy, r, paint);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            {
                posX = MathUtils.clamp(2 * event.getX() / getWidth() - 1.0f, -1, 1);
                posY = MathUtils.clamp(2 * event.getY() / getHeight() - 1.0f, -1, 1);
                break;
            }
            case MotionEvent.ACTION_UP: {
                posX = posY = 0;
                break;
            }
        }

        GamepadEmulator.updateStick(stickId, posX, posY);

        invalidate();
        return true;
    }
}

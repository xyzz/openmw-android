package ui.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.math.MathUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.libsdl.app.SDLActivity;

public class Joystick extends View {

    // Initial touch position
    protected float initialX, initialY;
    // Current touch position
    protected float currentX, currentY;
    // Whether the finger is down
    protected Boolean down = false;
    // left or right stick
    protected int stickId = 0;

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

        // Draw circle for initial touch
        if (down) {
            canvas.drawCircle(initialX, initialY, getWidth() / 10, paint);
        }

        // Draw circle for current stick position
        canvas.drawCircle(currentX, currentY, getWidth() / 5, paint);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                initialX = event.getX();
                initialY = event.getY();
                down = true;
            }
            case MotionEvent.ACTION_MOVE: {
                currentX = event.getX();
                currentY = event.getY();
                break;
            }
            case MotionEvent.ACTION_UP: {
                down = false;
                break;
            }
        }

        updateStick();
        invalidate();
        return true;
    }

    protected void updateStick() {
    }
}

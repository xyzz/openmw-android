package ui.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.core.math.MathUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class Joystick extends View {

    // Initial touch position
    protected float initialX, initialY;
    // Current touch position
    protected float currentX = -1, currentY = -1;
    // Whether the finger is down
    protected Boolean down = false;
    // left or right stick
    protected int stickId = 0;

    // width of a stroke
    private int width = 0;

    private Paint paint = new Paint();

    public Joystick(Context context) {
        super(context);
        init();
    }

    public Joystick(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Joystick(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init() {
        width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getContext().getResources().getDisplayMetrics());
    }

    public void setStick(int id) {
        stickId = id;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(width);
        paint.setColor(Color.GRAY);

        if (down) {
            // Draw initial touch
            canvas.drawCircle(initialX, initialY, getWidth() / 10f, paint);

            // Draw current stick position
            canvas.drawCircle(currentX, currentY, getWidth() / 5f, paint);
        } else {
            // Draw the outline
            canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,
                    getWidth() / 2f - width, paint);
        }
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
                currentX = currentY = -1;
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

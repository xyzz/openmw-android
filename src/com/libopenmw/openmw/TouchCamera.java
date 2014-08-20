package com.libopenmw.openmw;

import org.libsdl.app.SDLActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class TouchCamera extends View {

	private final String TAG = "JoystickView";
	private Paint circlePaint;

	private int innerPadding;

	public int count = 0;
	public double[] xmas = new double[3];
	public double[] ymas = new double[3];

	public TouchCamera(Context context) {
		super(context);
		initJoystickView();
	}

	public TouchCamera(Context context, AttributeSet attrs) {
		super(context, attrs);
		initJoystickView();
	}

	public TouchCamera(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initJoystickView();
	}

	private void initJoystickView() {

		setFocusable(true);

		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(Color.TRANSPARENT);
		circlePaint.setStrokeWidth(1);
		circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		int px = getMeasuredWidth() / 2;
		int py = getMeasuredHeight() / 2;
		int radius = Math.min(px, py);

		canvas.drawCircle(px, py, radius - innerPadding, circlePaint);

		canvas.save();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int actionType = event.getAction();
		if (actionType == MotionEvent.ACTION_DOWN) {
			xmas[0] = xmas[2] = event.getX();
			ymas[0] = ymas[2] = event.getY();
			final int touchDevId = event.getDeviceId();
			final int pointerCount = event.getPointerCount();
			int action = event.getActionMasked();
			int pointerFingerId = 0;
			int i = -1;
			SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
					MotionEvent.ACTION_UP, 0, 0, 0);
		}
		if (actionType == MotionEvent.ACTION_MOVE) {

			xmas[1] = event.getX();
			ymas[1] = event.getY();
			final int touchDevId = event.getDeviceId();
			final int pointerCount = event.getPointerCount();
			int action = event.getActionMasked();
			int pointerFingerId = 0;
			int i = -1;
			float x = 0, y = 0, p = 0;

			Log.d(TAG, "X:" + event.getX() + "|Y:" + event.getY());
			if (xmas[0] == xmas[1] && ymas[0] < ymas[1] && ymas[1]-ymas[0]>2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.5;

				y = (float) 0.9;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] == xmas[1] && ymas[0] > ymas[1] && ymas[0]-ymas[1]>2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.5;

				y = (float) 0.3;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			}

			else if (xmas[0] < xmas[1] && ymas[0] == ymas[1] && xmas[1]-xmas[0]>2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.9;

				y = (float) 0.5;

				p = event.getPressure(i);

				// Log.d(TAG, "X:" + x + "|Y:" + y);
				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] > xmas[1] && ymas[0] == ymas[1] && xmas[0]-xmas[1]>2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.3;

				y = (float) 0.5;

				p = event.getPressure(i);

				// Log.d(TAG, "X:" + x + "|Y:" + y);
				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] < xmas[1] && ymas[0] < ymas[1] && ymas[1]-ymas[0]>2 &&  xmas[1]-xmas[0]>2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.9;

				y = (float) 0.9;

				p = event.getPressure(i);

				// Log.d(TAG, "X:" + x + "|Y:" + y);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] > xmas[1] && ymas[0] > ymas[1] && ymas[0]-ymas[1]>2 &&  xmas[0]-xmas[1]>2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.3;

				y = (float) 0.3;

				p = event.getPressure(i);

				// Log.d(TAG, "X:" + x + "|Y:" + y);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] < xmas[1] && ymas[0] > ymas[1] && ymas[0]-ymas[1]>2 &&  xmas[1]-xmas[0]>2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.9;

				y = (float) 0.3;

				p = event.getPressure(i);

				// Log.d(TAG, "X:" + x + "|Y:" + y);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] > xmas[1] && ymas[0] < ymas[1] && ymas[1]-ymas[0]>2 &&  xmas[0]-xmas[1]>2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.3;

				y = (float) 0.9;

				p = event.getPressure(i);

				// Log.d(TAG, "X:" + x + "|Y:" + y);
				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_UP, 0, 0, 0);

			xmas[0] = xmas[1];
			ymas[0] = ymas[1];

			// Coordinates
			// Log.d(TAG, "X:" + touchX + "|Y:" + touchY);

			// Pressure

			invalidate();
		} else if (actionType == MotionEvent.ACTION_UP) {
			xmas[0] = xmas[1] = ymas[0] = ymas[1] = 0;

			final int touchDevId = event.getDeviceId();
			final int pointerCount = event.getPointerCount();
			int action = event.getActionMasked();
			int pointerFingerId = 0;
			int i = -1;
			float x = 0, y = 0, p = 0;
			SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
					MotionEvent.ACTION_UP, x, y, p);

		}
		return true;
	}
}

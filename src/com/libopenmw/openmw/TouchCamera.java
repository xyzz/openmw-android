package com.libopenmw.openmw;

import org.libsdl.app.SDLActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TouchCamera extends View {

	private Paint circlePaint;

	public double[] xmas = new double[2];
	public double[] ymas = new double[2];

	public TouchCamera(Context context) {
		super(context);
		initView();
	}

	public TouchCamera(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public TouchCamera(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	private void initView() {

		setFocusable(true);

		circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(Color.TRANSPARENT);
		circlePaint.setStrokeWidth(1);
		circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		int px = getMeasuredWidth();
		int py = getMeasuredHeight();

		canvas.drawPoint(px, py, circlePaint);
		canvas.save();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int actionType = event.getAction();
		if (actionType == MotionEvent.ACTION_DOWN) {
			xmas[0] = xmas[2] = event.getX();
			ymas[0] = ymas[2] = event.getY();
			final int touchDevId = event.getDeviceId();
			event.getPointerCount();
			event.getActionMasked();
			int pointerFingerId = 0;
			SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
					MotionEvent.ACTION_UP, 0, 0, 0);
		}
		if (actionType == MotionEvent.ACTION_MOVE) {

			Log.d("dwda", "X:" +  "|Y:" );
			xmas[1] = event.getRawX();
			ymas[1] = event.getRawY();
			final int touchDevId = event.getDeviceId();
			event.getPointerCount();
			event.getActionMasked();
			int pointerFingerId = 0;
			int i = -1;
			float x = 0, y = 0, p = 0;

			if (xmas[0] == xmas[1] && ymas[0] < ymas[1]
					&& ymas[1] - ymas[0] > 1.2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.5;

				y = (float) 0.9;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] == xmas[1] && ymas[0] > ymas[1]
					&& ymas[0] - ymas[1] > 1.2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.5;

				y = (float) 0.3;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			}

			else if (xmas[0] < xmas[1] && ymas[0] == ymas[1]
					&& xmas[1] - xmas[0] > 1.2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.9;

				y = (float) 0.5;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] > xmas[1] && ymas[0] == ymas[1]
					&& xmas[0] - xmas[1] > 1.2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.3;

				y = (float) 0.5;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] < xmas[1] && ymas[0] < ymas[1]
					&& ymas[1] - ymas[0] > 1.2 && xmas[1] - xmas[0] > 1.2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.9;

				y = (float) 0.9;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] > xmas[1] && ymas[0] > ymas[1]
					&& ymas[0] - ymas[1] > 1.2 && xmas[0] - xmas[1] > 1.2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.3;

				y = (float) 0.3;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] < xmas[1] && ymas[0] > ymas[1]
					&& ymas[0] - ymas[1] > 1.2 && xmas[1] - xmas[0] > 1.2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.9;

				y = (float) 0.3;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] > xmas[1] && ymas[0] < ymas[1]
					&& ymas[1] - ymas[0] > 1.2 && xmas[0] - xmas[1] > 1.2)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.3;

				y = (float) 0.9;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_UP, 0, 0, 0);

			xmas[0] = xmas[1];
			ymas[0] = ymas[1];

			invalidate();
		} else if (actionType == MotionEvent.ACTION_UP) {
			xmas[0] = xmas[1] = ymas[0] = ymas[1] = 0;

			final int touchDevId = event.getDeviceId();
			event.getPointerCount();
			event.getActionMasked();
			int pointerFingerId = 0;
			float x = 0, y = 0, p = 0;
			SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
					MotionEvent.ACTION_UP, x, y, p);

		}
		return true;
	}
}

package ui.controls;

import org.libsdl.app.SDLActivity;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class TouchCameraSimulation extends View {

	private float constTouch = 0;
	private float[] xmas = new float[2];
	private float[] ymas = new float[2];

	public TouchCameraSimulation(Context context) {
		super(context);
		initView();
		cameraTouchConst(context);
	}

	public TouchCameraSimulation(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		cameraTouchConst(context);

	}

	public TouchCameraSimulation(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView();
		cameraTouchConst(context);

	}

	private void initView() {

		setFocusable(true);

	}

	private double tabletSize(Context context) {

		double size = 0;
		try {

			// Compute screen size

			DisplayMetrics dm = context.getResources().getDisplayMetrics();

			float screenWidth = dm.widthPixels / dm.xdpi;

			float screenHeight = dm.heightPixels / dm.ydpi;

			size = Math.sqrt(Math.pow(screenWidth, 2) +

			Math.pow(screenHeight, 2));

		} catch (Throwable t) {

		}

		return size;

	}

	private void cameraTouchConst(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();

		if (tabletSize(context) >= 7.0)
			constTouch = 0;
		else
			constTouch = (float) display.getHeight() / display.getWidth();

	}

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				simulateCameraMovement(event);
			}
		}).start();
		return true;
	}


	private void simulateCameraMovement(MotionEvent event){
		int actionType = event.getAction();
		if (actionType == MotionEvent.ACTION_DOWN) {
			xmas[0] = event.getX();
			ymas[0] = event.getY();
			final int touchDevId = event.getDeviceId();
			event.getPointerCount();
			event.getActionMasked();
			int pointerFingerId = 0;
			SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
					MotionEvent.ACTION_UP, 0, 0, 0);
		}
		if (actionType == MotionEvent.ACTION_MOVE) {

			xmas[1] = event.getRawX();
			ymas[1] = event.getRawY();
			final int touchDevId = event.getDeviceId();
			event.getPointerCount();
			event.getActionMasked();
			int pointerFingerId = 0;
			int i = -1;
			float x = 0, y = 0, p = 0;

			if (xmas[0] == xmas[1] && ymas[0] < ymas[1]
					&& ymas[1] - ymas[0] > constTouch)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.5;

				y = (float) 0.9;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] == xmas[1] && ymas[0] > ymas[1]
					&& ymas[0] - ymas[1] > constTouch)

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
					&& xmas[1] - xmas[0] > constTouch)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.9;

				y = (float) 0.5;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] > xmas[1] && ymas[0] == ymas[1]
					&& xmas[0] - xmas[1] > constTouch)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.3;

				y = (float) 0.5;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] < xmas[1] && ymas[0] < ymas[1]
					&& ymas[1] - ymas[0] > constTouch
					&& xmas[1] - xmas[0] > constTouch)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.9;

				y = (float) 0.9;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] > xmas[1] && ymas[0] > ymas[1]
					&& ymas[0] - ymas[1] > constTouch
					&& xmas[0] - xmas[1] > constTouch)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.3;

				y = (float) 0.3;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] < xmas[1] && ymas[0] > ymas[1]
					&& ymas[0] - ymas[1] > constTouch
					&& xmas[1] - xmas[0] > constTouch)

			{
				i = event.getActionIndex();

				pointerFingerId = event.getPointerId(i);

				x = (float) 0.9;

				y = (float) 0.3;

				p = event.getPressure(i);

				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_MOVE, x, y, p);
			} else if (xmas[0] > xmas[1] && ymas[0] < ymas[1]
					&& ymas[1] - ymas[0] > constTouch
					&& xmas[0] - xmas[1] > constTouch)

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

	}

}

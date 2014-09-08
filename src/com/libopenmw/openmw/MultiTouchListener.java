package com.libopenmw.openmw;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class MultiTouchListener implements OnTouchListener {

	private float mPrevX;
	private float mPrevY;

	public ConfigureControls Activity;

	public MultiTouchListener(ConfigureControls configureControls) {
		Activity = configureControls;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		float currX, currY;
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN: {

			mPrevX = event.getX();
			mPrevY = event.getY();
			break;
		}

		case MotionEvent.ACTION_MOVE: {

			currX = event.getRawX();
			currY = event.getRawY();

			MarginLayoutParams marginParams = new MarginLayoutParams(
					view.getLayoutParams());
			marginParams.setMargins((int) (currX - mPrevX),
					(int) (currY - mPrevY), 0, 0);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
					marginParams);
			view.setLayoutParams(layoutParams);

			break;
		}

		case MotionEvent.ACTION_CANCEL:
			break;

		case MotionEvent.ACTION_UP:
			ConfigureControls.buttonFlag = view.getId();
			if (view.getId() == 1)
				ConfigureControls.button.setText("buttonRun");
			else if (view.getId() == 2)
				ConfigureControls.button.setText("buttonConsole");
			else if (view.getId() == 3)
				ConfigureControls.button.setText("buttonPerson");
			else if (view.getId() == 4)
				ConfigureControls.button.setText("buttonWait");
			else if (view.getId() == 5)
				ConfigureControls.button.setText("buttonTouchon");
			else if (view.getId() == 6)
				ConfigureControls.button.setText("buttonDiary");
			else if (view.getId() == 7)
				ConfigureControls.button.setText("buttonPause");
			else if (view.getId() == 8)
				ConfigureControls.button.setText("buttonLoad");
			else if (view.getId() == 9)
				ConfigureControls.button.setText("buttonSave");
			else if (view.getId() == 10)
				ConfigureControls.button.setText("buttonWeapon");
			else if (view.getId() == 11)
				ConfigureControls.button.setText("buttonInventory");
			else if (view.getId() == 12)
				ConfigureControls.button.setText("buttonJump");
			else if (view.getId() == 13)
				ConfigureControls.button.setText("buttonFire");
			else if (view.getId() == 14)
				ConfigureControls.button.setText("buttonMagic");
			else if (view.getId() == 15)
				ConfigureControls.button.setText("buttonUse");
			if (view.getId() == 16)
				ConfigureControls.button.setText("buttonCrouch");
			if (view.getId() == 17)
				ConfigureControls.button.setText("joystick");

			break;
		}

		return true;
	}

}
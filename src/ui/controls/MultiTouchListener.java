package ui.controls;

import ui.activity.ConfigureControls;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class MultiTouchListener implements OnTouchListener {

	private float mPrevX;
	private float mPrevY;

	public MultiTouchListener() {

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

			switch (view.getId()) {
			case 1:
				ConfigureControls.button.setText("buttonRun");
				break;

			case 2:
				ConfigureControls.button.setText("buttonConsole");
				break;
			case 3:
				ConfigureControls.button.setText("buttonPerson");
				break;
			case 4:
				ConfigureControls.button.setText("buttonWait");
				break;
			case 5:
				ConfigureControls.button.setText("buttonTouchon");
				break;
			case 6:
				ConfigureControls.button.setText("buttonDiary");
				break;
			case 7:
				ConfigureControls.button.setText("buttonPause");
				break;
			case 8:
				ConfigureControls.button.setText("buttonLoad");
				break;
			case 9:
				ConfigureControls.button.setText("buttonSave");
				break;
			case 10:
				ConfigureControls.button.setText("buttonWeapon");
				break;
			case 11:
				ConfigureControls.button.setText("buttonInventory");
				break;
			case 12:
				ConfigureControls.button.setText("buttonJump");
				break;
			case 13:
				ConfigureControls.button.setText("buttonFire");
				break;
			case 14:
				ConfigureControls.button.setText("buttonMagic");
				break;
			case 15:
				ConfigureControls.button.setText("buttonUse");
				break;
			case 16:
				ConfigureControls.button.setText("buttonCrouch");
				break;
			case 17:
				ConfigureControls.button.setText("joystick");
				break;
			case 18:
				ConfigureControls.button.setText("F1");
				break;
			case 19:
				ConfigureControls.button.setText("showPanel");
				break;
			case 20:
				ConfigureControls.button.setText("key0");
				break;
			case 21:
				ConfigureControls.button.setText("key1");
				break;
			case 22:
				ConfigureControls.button.setText("key2");
				break;
			case 23:
				ConfigureControls.button.setText("key3");
				break;
			case 24:
				ConfigureControls.button.setText("key4");
				break;
			case 25:
				ConfigureControls.button.setText("key5");
				break;
			case 26:
				ConfigureControls.button.setText("key6");
				break;
			case 27:
				ConfigureControls.button.setText("key7");
				break;
			case 28:
				ConfigureControls.button.setText("key8");
				break;
			case 29:
				ConfigureControls.button.setText("key9");
				break;

			}

			break;
		}

		return true;
	}
}
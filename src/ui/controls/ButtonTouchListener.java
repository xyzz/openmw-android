package ui.controls;

import org.libsdl.app.SDLActivity;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ButtonTouchListener implements OnTouchListener {

	private int keyCode;

	public ButtonTouchListener(int keyCode) {
		this.keyCode = keyCode;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			SDLActivity.onNativeKeyDown(keyCode);
			return true;
		case MotionEvent.ACTION_UP:
			SDLActivity.onNativeKeyUp(keyCode);
			return true;
		}
		return false;
	}

}
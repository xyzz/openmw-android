package com.libopenmw.openmw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageButton;

public class ConfigureControls extends Activity {

	public Context context;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.screencontrols);

		
		context = this;
		
		 DisplayMetrics displaymetrics;
			displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		CoordinatesAllScreens.height = displaymetrics.heightPixels;
		CoordinatesAllScreens.width = displaymetrics.widthPixels;
		final MultiTouchListener touchListener = new MultiTouchListener(this);
		final Controls joystick = (Controls) findViewById(R.id.joystick);
		joystick.setOnTouchListener(touchListener);

		joystick.setLayoutParams(ControlsParams.coordinates(joystick, 20, 400,
				250, 250));

		final ImageButton buttonRun = (ImageButton) findViewById(R.id.buttonrun1);
		buttonRun.setOnTouchListener(touchListener);

		buttonRun.setLayoutParams(ControlsParams.coordinates(buttonRun, 10,
				330, 70, 70));

		final ImageButton buttonConsole = (ImageButton) findViewById(R.id.buttonconsole);
		buttonConsole.setOnTouchListener(touchListener);

		buttonConsole.setLayoutParams(ControlsParams.coordinates(buttonConsole,
				140, 0, 70, 70));

		final ImageButton buttonChangePerson = (ImageButton) findViewById(R.id.buttonchangeperson);
		buttonChangePerson.setOnTouchListener(touchListener);

		buttonChangePerson.setLayoutParams(ControlsParams.coordinates(buttonChangePerson,
				212, 0, 70, 70));
		
		final ImageButton buttonWait = (ImageButton) findViewById(R.id.buttonwait);
		buttonWait.setOnTouchListener(touchListener);

		buttonWait.setLayoutParams(ControlsParams.coordinates(buttonWait,
				274, 0, 70, 70));
		
	
		
		final Button buttonTouch = (Button) findViewById(R.id.buttontouch);
		buttonTouch.setOnTouchListener(touchListener);

		buttonTouch.setLayoutParams(ControlsParams.coordinates(buttonTouch,
				346, 0, 70, 70));
		buttonTouch.setAlpha((float) 0.5);
			final ImageButton buttonDiary = (ImageButton) findViewById(R.id.buttonDiary);
		buttonDiary.setOnTouchListener(touchListener);

		buttonDiary.setLayoutParams(ControlsParams.coordinates(buttonDiary,
				414, 0, 70, 70));

		final ImageButton buttonPause = (ImageButton) findViewById(R.id.buttonpause);
		buttonPause.setOnTouchListener(touchListener);

		buttonPause.setLayoutParams(ControlsParams.coordinates(buttonPause,
				950, 0, 60, 60));
		
		final ImageButton buttonLoad= (ImageButton) findViewById(R.id.buttonsuperload);
		buttonLoad.setOnTouchListener(touchListener);

		buttonLoad.setLayoutParams(ControlsParams.coordinates(buttonLoad,
				880, 0, 60, 60));

		final ImageButton buttonSave= (ImageButton) findViewById(R.id.buttonsupersave);
		buttonSave.setOnTouchListener(touchListener);

		buttonSave.setLayoutParams(ControlsParams.coordinates(buttonSave,
				820, 0, 60, 60));

		
		final ImageButton buttonWeapon = (ImageButton) findViewById(R.id.buttonweapon);

		buttonWeapon.setOnTouchListener(touchListener);

		buttonWeapon.setLayoutParams(ControlsParams.coordinates(buttonWeapon,
				880, 95, 70, 70));
		
		final ImageButton buttonInventory = (ImageButton) findViewById(R.id.buttoninventory);

		buttonInventory.setOnTouchListener(touchListener);

		buttonInventory.setLayoutParams(ControlsParams.coordinates(buttonInventory,
				950, 95, 70, 70));
		
		final ImageButton buttonJump = (ImageButton) findViewById(R.id.buttonsuperjump);

		buttonJump.setOnTouchListener(touchListener);

		buttonJump.setLayoutParams(ControlsParams.coordinates(buttonJump,
				920, 195, 90, 90));
		
		final ImageButton buttonFire = (ImageButton) findViewById(R.id.buttonFire);

		buttonFire.setOnTouchListener(touchListener);

		buttonFire.setLayoutParams(ControlsParams.coordinates(buttonFire,
				790, 300, 100, 100));
		
		final ImageButton buttonMagic = (ImageButton) findViewById(R.id.buttonMagic);

		buttonMagic.setOnTouchListener(touchListener);

		buttonMagic.setLayoutParams(ControlsParams.coordinates(buttonMagic,
				940, 480, 80, 80));
		final ImageButton buttonUse = (ImageButton) findViewById(R.id.buttonUse);

		buttonUse.setOnTouchListener(touchListener);

		buttonUse.setLayoutParams(ControlsParams.coordinates(buttonUse,
				940, 368, 80, 80));

		final ImageButton buttonCrouch = (ImageButton) findViewById(R.id.buttoncrouch);

		buttonCrouch.setOnTouchListener(touchListener);

		buttonCrouch.setLayoutParams(ControlsParams.coordinates(buttonCrouch,
				940, 670, 80, 80));

	}

}

package com.libopenmw.openmw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
		final MultiTouchListener touchListener = new MultiTouchListener(this);
		final Controls joystick = (Controls) findViewById(R.id.joystick);
		joystick.setOnTouchListener(touchListener);

		joystick.setLayoutParams(ControlsParams.coordinates(joystick, 20, 400,
				300, 300));

		final ImageButton buttonRun = (ImageButton) findViewById(R.id.buttonrun1);
		buttonRun.setOnTouchListener(touchListener);

		buttonRun.setLayoutParams(ControlsParams.coordinates(buttonRun, 10,
				330, 80, 80));

		final ImageButton buttonConsole = (ImageButton) findViewById(R.id.buttonconsole);
		buttonConsole.setOnTouchListener(touchListener);

		buttonConsole.setLayoutParams(ControlsParams.coordinates(buttonConsole,
				160, 0, 80, 80));

		final ImageButton buttonChangePerson = (ImageButton) findViewById(R.id.buttonchangeperson);
		buttonChangePerson.setOnTouchListener(touchListener);

		buttonChangePerson.setLayoutParams(ControlsParams.coordinates(buttonChangePerson,
				242, 0, 80, 80));
		
		final ImageButton buttonWait = (ImageButton) findViewById(R.id.buttonwait);
		buttonWait.setOnTouchListener(touchListener);

		buttonWait.setLayoutParams(ControlsParams.coordinates(buttonWait,
				324, 0, 80, 80));
		

		final Button buttonTouch = (Button) findViewById(R.id.buttontouch);
		buttonTouch.setOnTouchListener(touchListener);

		buttonTouch.setLayoutParams(ControlsParams.coordinates(buttonTouch,
				406, 0, 80, 80));

		final ImageButton buttonPause = (ImageButton) findViewById(R.id.buttonpause);
		buttonPause.setOnTouchListener(touchListener);

		buttonPause.setLayoutParams(ControlsParams.coordinates(buttonPause,
				1200, 0, 80, 80));
		
		final ImageButton buttonLoad= (ImageButton) findViewById(R.id.buttonsuperload);
		buttonLoad.setOnTouchListener(touchListener);

		buttonLoad.setLayoutParams(ControlsParams.coordinates(buttonLoad,
				1118, 0, 80, 80));

		final ImageButton buttonSave= (ImageButton) findViewById(R.id.buttonsupersave);
		buttonSave.setOnTouchListener(touchListener);

		buttonSave.setLayoutParams(ControlsParams.coordinates(buttonSave,
				1036, 0, 80, 80));

		
		final ImageButton buttonWeapon = (ImageButton) findViewById(R.id.buttonweapon);

		buttonWeapon.setOnTouchListener(touchListener);

		buttonWeapon.setLayoutParams(ControlsParams.coordinates(buttonWeapon,
				1118, 82, 80, 80));
		
		final ImageButton buttonInventory = (ImageButton) findViewById(R.id.buttoninventory);

		buttonInventory.setOnTouchListener(touchListener);

		buttonInventory.setLayoutParams(ControlsParams.coordinates(buttonInventory,
				1200, 82, 80, 80));
		
		final ImageButton buttonJump = (ImageButton) findViewById(R.id.buttonsuperjump);

		buttonJump.setOnTouchListener(touchListener);

		buttonJump.setLayoutParams(ControlsParams.coordinates(buttonJump,
				1160, 164, 110, 110));
		
		final ImageButton buttonFire = (ImageButton) findViewById(R.id.buttonFire);

		buttonFire.setOnTouchListener(touchListener);

		buttonFire.setLayoutParams(ControlsParams.coordinates(buttonFire,
				1000, 264, 120, 120));
		
		final ImageButton buttonMagic = (ImageButton) findViewById(R.id.buttonMagic);

		buttonMagic.setOnTouchListener(touchListener);

		buttonMagic.setLayoutParams(ControlsParams.coordinates(buttonMagic,
				1200, 364, 100, 100));

		final ImageButton buttonCrouch = (ImageButton) findViewById(R.id.buttoncrouch);

		buttonCrouch.setOnTouchListener(touchListener);

		buttonCrouch.setLayoutParams(ControlsParams.coordinates(buttonCrouch,
				1180, 610, 100, 100));

	}

}

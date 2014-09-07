package com.libopenmw.openmw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ConfigureControls extends Activity {

	public Context context;

	public Controls joystick;
	public ImageButton buttonRun;
	public ImageButton buttonConsole;
	public ImageButton buttonChangePerson;
	public ImageButton buttonWait;
	public Button buttonSize;
	public Button buttonSize1;
	public Button buttonTouch;
	
	public Button buttonOpacity;
	public Button buttonOpacity1;
	public TextView opacity;
	public TextView size;
	public TextView button;

	public ImageButton buttonDiary;
	public ImageButton buttonPause;
	public ImageButton buttonLoad;
	public ImageButton buttonSave;
	public ImageButton buttonWeapon;
	public ImageButton buttonInventory;
	public ImageButton buttonJump;
	public ImageButton buttonFire;
	public ImageButton buttonMagic;
	public ImageButton buttonUse;
	public ImageButton buttonCrouch;
	

	public SharedPreferences Settings;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.screencontrols);

		int controlsFlag;

		context = this;

		Settings = getSharedPreferences(Constants.APP_PREFERENCES,
				Context.MODE_MULTI_PROCESS);
		controlsFlag = Settings.getInt(
				Constants.APP_PREFERENCES_RESET_CONTROLS, -1);
		DisplayMetrics displaymetrics;
		displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		CoordinatesAllScreens.height = displaymetrics.heightPixels;
		CoordinatesAllScreens.width = displaymetrics.widthPixels;
		final MultiTouchListener touchListener = new MultiTouchListener(this);
		
		buttonSize = (Button) findViewById(R.id.buttonsize1);
		buttonSize1 = (Button) findViewById(R.id.buttonsize2);
		button=(TextView) findViewById(R.id.textViewTitle);
		size=(TextView) findViewById(R.id.textViewSize);
			
		buttonSize.setTextColor(Color.WHITE);
		buttonSize.setTextSize((float) CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(40));
		buttonSize.setLayoutParams(ControlsParams.coordinates(
				buttonSize, 400, 280, 70, 70));
		buttonSize.setVisibility(Button.VISIBLE);
		
		size.setTextColor(Color.WHITE);
		size.setTextSize((float) CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(30));
		size.setLayoutParams(ControlsParams.coordinates(
				size, 463, 300, 150, 150));
		size.setVisibility(TextView.VISIBLE);
		
		buttonSize1.setTextColor(Color.WHITE);
		buttonSize1.setTextSize((float) CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(40));
		buttonSize1.setLayoutParams(ControlsParams.coordinates(
				buttonSize1, 570, 280, 70, 80));
		buttonSize1.setVisibility(Button.VISIBLE);
		
		buttonOpacity = (Button) findViewById(R.id.buttonopacity1);
		buttonOpacity1 = (Button) findViewById(R.id.buttonopacity2);

		opacity=(TextView) findViewById(R.id.textViewOpacity);
			
		buttonOpacity.setTextColor(Color.WHITE);
		buttonOpacity.setTextSize((float) CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(40));
		buttonOpacity.setLayoutParams(ControlsParams.coordinates(
				buttonOpacity, 400, 355, 70, 70));
		buttonOpacity.setVisibility(Button.VISIBLE);
		
		opacity.setTextColor(Color.WHITE);
		opacity.setTextSize((float) CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(30));
		opacity.setLayoutParams(ControlsParams.coordinates(
				opacity, 463, 368, 200, 150));
		opacity.setVisibility(TextView.VISIBLE);
		
		buttonOpacity1.setTextColor(Color.WHITE);
		buttonOpacity1.setTextSize((float) CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(40));
		buttonOpacity1.setLayoutParams(ControlsParams.coordinates(
				buttonOpacity1, 650, 355, 70, 80));
		buttonOpacity1.setVisibility(Button.VISIBLE);
		
		
		button.setTextColor(Color.WHITE);
		button.setTextSize((float) CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(30));
		button.setLayoutParams(ControlsParams.coordinates(
				button, 450, 220, 200, 150));
		button.setVisibility(TextView.VISIBLE);
		
		joystick = (Controls) findViewById(R.id.joystick);
		joystick.setOnTouchListener(touchListener);

		joystick.setAlpha((float) 0.5);

		buttonRun = (ImageButton) findViewById(R.id.buttonrun1);
		
		
		buttonRun.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					
					buttonRun.setOnTouchListener(touchListener);
									
					return true; 
				case MotionEvent.ACTION_UP:
					
					
					return true; 
				}
				return false;
			}
		});


		buttonRun.setAlpha((float) 0.5);

		buttonConsole = (ImageButton) findViewById(R.id.buttonconsole);
		buttonConsole.setOnTouchListener(touchListener);

		buttonConsole.setAlpha((float) 0.5);

		buttonChangePerson = (ImageButton) findViewById(R.id.buttonchangeperson);
		buttonChangePerson.setOnTouchListener(touchListener);

		buttonChangePerson.setAlpha((float) 0.5);

		buttonWait = (ImageButton) findViewById(R.id.buttonwait);
		buttonWait.setOnTouchListener(touchListener);

		buttonWait.setAlpha((float) 0.5);

		buttonTouch = (Button) findViewById(R.id.buttontouch);
		buttonTouch.setOnTouchListener(touchListener);

		buttonTouch.setAlpha((float) 0.5);
		buttonDiary = (ImageButton) findViewById(R.id.buttonDiary);
		buttonDiary.setOnTouchListener(touchListener);

		buttonDiary.setAlpha((float) 0.5);

		buttonPause = (ImageButton) findViewById(R.id.buttonpause);
		buttonPause.setOnTouchListener(touchListener);

		buttonPause.setAlpha((float) 0.5);

		buttonLoad = (ImageButton) findViewById(R.id.buttonsuperload);
		buttonLoad.setOnTouchListener(touchListener);

		buttonLoad.setAlpha((float) 0.5);

		buttonSave = (ImageButton) findViewById(R.id.buttonsupersave);
		buttonSave.setOnTouchListener(touchListener);

		buttonSave.setAlpha((float) 0.5);

		buttonWeapon = (ImageButton) findViewById(R.id.buttonweapon);

		buttonWeapon.setOnTouchListener(touchListener);

		buttonWeapon.setAlpha((float) 0.5);

		buttonInventory = (ImageButton) findViewById(R.id.buttoninventory);

		buttonInventory.setOnTouchListener(touchListener);

		buttonInventory.setAlpha((float) 0.5);

		buttonJump = (ImageButton) findViewById(R.id.buttonsuperjump);

		buttonJump.setOnTouchListener(touchListener);

		buttonJump.setAlpha((float) 0.5);

		buttonFire = (ImageButton) findViewById(R.id.buttonFire);

		buttonFire.setOnTouchListener(touchListener);

		buttonFire.setAlpha((float) 0.5);

		buttonMagic = (ImageButton) findViewById(R.id.buttonMagic);

		buttonMagic.setOnTouchListener(touchListener);

		buttonMagic.setAlpha((float) 0.5);

		buttonUse = (ImageButton) findViewById(R.id.buttonUse);

		buttonUse.setOnTouchListener(touchListener);

		buttonUse.setAlpha((float) 0.5);

		buttonCrouch = (ImageButton) findViewById(R.id.buttoncrouch);

		buttonCrouch.setOnTouchListener(touchListener);

		buttonCrouch.setAlpha((float) 0.5);

		if (controlsFlag == -1 || controlsFlag == 1) {
			joystick.setLayoutParams(ControlsParams.coordinates(joystick, 20,
					400, 250, 250));
			buttonRun.setLayoutParams(ControlsParams.coordinates(buttonRun, 10,
					330, 70, 70));
			buttonConsole.setLayoutParams(ControlsParams.coordinates(
					buttonConsole, 140, 0, 70, 70));
			buttonChangePerson.setLayoutParams(ControlsParams.coordinates(
					buttonChangePerson, 212, 0, 70, 70));
			buttonWait.setLayoutParams(ControlsParams.coordinates(buttonWait,
					274, 0, 70, 70));
			buttonTouch.setLayoutParams(ControlsParams.coordinates(buttonTouch,
					346, 0, 70, 70));
			buttonWeapon.setLayoutParams(ControlsParams.coordinates(
					buttonWeapon, 880, 95, 70, 70));
			buttonDiary.setLayoutParams(ControlsParams.coordinates(buttonDiary,
					414, 0, 70, 70));
			buttonPause.setLayoutParams(ControlsParams.coordinates(buttonPause,
					950, 0, 60, 60));
			buttonLoad.setLayoutParams(ControlsParams.coordinates(buttonLoad,
					880, 0, 60, 60));
			buttonSave.setLayoutParams(ControlsParams.coordinates(buttonSave,
					820, 0, 60, 60));
			buttonInventory.setLayoutParams(ControlsParams.coordinates(
					buttonInventory, 950, 95, 70, 70));
			buttonJump.setLayoutParams(ControlsParams.coordinates(buttonJump,
					920, 195, 90, 90));
			buttonFire.setLayoutParams(ControlsParams.coordinates(buttonFire,
					790, 300, 100, 100));
			buttonMagic.setLayoutParams(ControlsParams.coordinates(buttonMagic,
					940, 480, 80, 80));
			buttonUse.setLayoutParams(ControlsParams.coordinates(buttonUse,
					940, 368, 80, 80));
			buttonCrouch.setLayoutParams(ControlsParams.coordinates(
					buttonCrouch, 940, 670, 80, 80));

			Editor editor = Settings.edit();
			editor.putInt(Constants.APP_PREFERENCES_RESET_CONTROLS, 0);
			editor.apply();
		} else if (controlsFlag == 0) {
			joystick.setLayoutParams(ControlsParams
					.coordinatesConfigureControls(joystick, Settings.getInt(
							Constants.APP_PREFERENCES_JOYSTICK_X, -1), Settings
							.getInt(Constants.APP_PREFERENCES_JOYSTICK_Y, -1),
							250, 250));
			buttonRun
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonRun,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_RUN_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_RUN_Y, -1),
							70, 70));
			buttonConsole.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					buttonConsole, Settings.getInt(
							Constants.APP_PREFERENCES_BUTTON_CONSOLE_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_BUTTON_CONSOLE_Y,
							-1), 70, 70));
			buttonChangePerson
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonChangePerson,
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_X,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_Y,
											-1), 70, 70));
			buttonWait
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonWait,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_WAIT_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_WAIT_Y, -1),
							70, 70));
			buttonTouch.setLayoutParams(ControlsParams
					.coordinatesConfigureControls(buttonTouch, Settings.getInt(
							Constants.APP_PREFERENCES_BUTTON_TOUCH_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_TOUCH_Y,
									-1), 70, 70));
			buttonWeapon.setLayoutParams(ControlsParams
					.coordinatesConfigureControls(buttonWeapon, Settings
							.getInt(Constants.APP_PREFERENCES_BUTTON_WEAPON_X,
									-1), Settings.getInt(
							Constants.APP_PREFERENCES_BUTTON_WEAPON_Y, -1), 70,
							70));
			buttonDiary.setLayoutParams(ControlsParams
					.coordinatesConfigureControls(buttonDiary, Settings.getInt(
							Constants.APP_PREFERENCES_BUTTON_DIARY_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_DIARY_Y,
									-1), 70, 70));
			buttonPause.setLayoutParams(ControlsParams
					.coordinatesConfigureControls(buttonPause, Settings.getInt(
							Constants.APP_PREFERENCES_BUTTON_PAUSE_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_PAUSE_Y,
									-1), 60, 60));
			buttonLoad
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonLoad,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_LOAD_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_LOAD_Y, -1),
							60, 60));
			buttonSave
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonSave,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_SAVE_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_SAVE_Y, -1),
							60, 60));
			buttonInventory
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonInventory,
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_INVENTORY_X,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_INVENTORY_Y,
											-1), 70, 70));
			buttonJump
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonJump,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_JUMP_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_JUMP_Y, -1),
							90, 90));
			buttonFire
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonFire,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_FIRE_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_FIRE_Y, -1),
							100, 100));
			buttonMagic.setLayoutParams(ControlsParams
					.coordinatesConfigureControls(buttonMagic, Settings.getInt(
							Constants.APP_PREFERENCES_BUTTON_MAGIC_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_MAGIC_Y,
									-1), 80, 80));
			buttonUse
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonUse,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_USE_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_USE_Y, -1),
							80, 80));
			buttonCrouch.setLayoutParams(ControlsParams
					.coordinatesConfigureControls(buttonCrouch, Settings
							.getInt(Constants.APP_PREFERENCES_BUTTON_CROUCH_X,
									-1), Settings.getInt(
							Constants.APP_PREFERENCES_BUTTON_CROUCH_Y, -1), 80,
							80));
			
		}

	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		Editor editor = Settings.edit();
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_RUN_X,
				(int) buttonRun.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_RUN_Y,
				(int) buttonRun.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CONSOLE_X,
				(int) buttonConsole.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CONSOLE_Y,
				(int) buttonConsole.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_X,
				(int) buttonChangePerson.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_Y,
				(int) buttonChangePerson.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_WAIT_X,
				(int) buttonWait.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_WAIT_Y,
				(int) buttonWait.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_TOUCH_X,
				(int) buttonTouch.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_TOUCH_Y,
				(int) buttonTouch.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_DIARY_X,
				(int) buttonDiary.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_DIARY_Y,
				(int) buttonDiary.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_SAVE_X,
				(int) buttonSave.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_SAVE_Y,
				(int) buttonSave.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_LOAD_X,
				(int) buttonLoad.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_LOAD_Y,
				(int) buttonLoad.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_PAUSE_X,
				(int) buttonPause.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_PAUSE_Y,
				(int) buttonPause.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_WEAPON_X,
				(int) buttonWeapon.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_WEAPON_Y,
				(int) buttonWeapon.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_INVENTORY_X,
				(int) buttonInventory.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_INVENTORY_Y,
				(int) buttonInventory.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_JUMP_X,
				(int) buttonJump.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_JUMP_Y,
				(int) buttonJump.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_FIRE_X,
				(int) buttonFire.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_FIRE_Y,
				(int) buttonFire.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_USE_X,
				(int) buttonUse.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_USE_Y,
				(int) buttonUse.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_MAGIC_X,
				(int) buttonMagic.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_MAGIC_Y,
				(int) buttonMagic.getY());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CROUCH_X,
				(int) buttonCrouch.getX());
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CROUCH_Y,
				(int) buttonCrouch.getY());
		editor.putInt(Constants.APP_PREFERENCES_JOYSTICK_X,
				(int) joystick.getX());
		editor.putInt(Constants.APP_PREFERENCES_JOYSTICK_Y,
				(int) joystick.getY());
		editor.apply();
	}

}

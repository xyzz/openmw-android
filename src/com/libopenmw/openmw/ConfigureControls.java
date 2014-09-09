package com.libopenmw.openmw;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

	public static TextView button;

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
	public static int buttonFlag = 0;

	public static boolean buttonOpacityFlag = false;
	public static boolean buttonSizeFlag = false;
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
		button = (TextView) findViewById(R.id.textViewTitle);

		buttonSize.setLayoutParams(ControlsParams.coordinates(buttonSize, 400,
				280, 100, 80));
		buttonSize.setVisibility(Button.VISIBLE);
		buttonSize.setAlpha((float) 0.5);

		buttonSize1.setLayoutParams(ControlsParams.coordinates(buttonSize1,
				520, 280, 100, 80));
		buttonSize1.setAlpha((float) 0.5);

		buttonSize1.setVisibility(Button.VISIBLE);

		buttonOpacity = (Button) findViewById(R.id.buttonopacity1);
		buttonOpacity1 = (Button) findViewById(R.id.buttonopacity2);

		buttonOpacity.setLayoutParams(ControlsParams.coordinates(buttonOpacity,
				400, 400, 120, 100));
		buttonOpacity.setVisibility(Button.VISIBLE);

		buttonOpacity1.setLayoutParams(ControlsParams.coordinates(
				buttonOpacity1, 540, 400, 120, 100));
		buttonOpacity1.setVisibility(Button.VISIBLE);

		buttonOpacity.setAlpha((float) 0.5);
		buttonOpacity1.setAlpha((float) 0.5);

		button.setTextColor(Color.WHITE);
		button.setTextSize((float) CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(20));
		button.setLayoutParams(ControlsParams.coordinates(button, 400, 210,
				400, 150));
		button.setVisibility(TextView.VISIBLE);

		buttonOpacity.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					buttonOpacityFlag = false;
					setButtonsOpacity();
					return true;
				case MotionEvent.ACTION_UP:

					return true;
				}
				return false;
			}
		});

		buttonOpacity1.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					buttonOpacityFlag = true;
					setButtonsOpacity();
					return true;
				case MotionEvent.ACTION_UP:

					return true;
				}
				return false;
			}
		});

		buttonSize.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					buttonSizeFlag = false;
					setButtonsSize();
					return true;
				case MotionEvent.ACTION_UP:

					return true;
				}
				return false;
			}
		});

		buttonSize1.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					buttonSizeFlag = true;
					setButtonsSize();
					return true;
				case MotionEvent.ACTION_UP:

					return true;
				}
				return false;
			}
		});

		joystick = (Controls) findViewById(R.id.joystick);
		joystick.setOnTouchListener(touchListener);
		joystick.setId(17);
		buttonRun = (ImageButton) findViewById(R.id.buttonrun1);
		buttonRun.setId(1);
		buttonRun.setOnTouchListener(touchListener);

		buttonConsole = (ImageButton) findViewById(R.id.buttonconsole);
		buttonConsole.setOnTouchListener(touchListener);
		buttonConsole.setId(2);

		buttonChangePerson = (ImageButton) findViewById(R.id.buttonchangeperson);
		buttonChangePerson.setOnTouchListener(touchListener);
		buttonChangePerson.setId(3);

		buttonWait = (ImageButton) findViewById(R.id.buttonwait);
		buttonWait.setOnTouchListener(touchListener);
		buttonWait.setId(4);

		buttonTouch = (Button) findViewById(R.id.buttontouch);
		buttonTouch.setOnTouchListener(touchListener);
		buttonTouch.setId(5);
		buttonDiary = (ImageButton) findViewById(R.id.buttonDiary);
		buttonDiary.setOnTouchListener(touchListener);
		buttonDiary.setId(6);

		buttonPause = (ImageButton) findViewById(R.id.buttonpause);
		buttonPause.setOnTouchListener(touchListener);
		buttonPause.setId(7);

		buttonLoad = (ImageButton) findViewById(R.id.buttonsuperload);
		buttonLoad.setOnTouchListener(touchListener);
		buttonLoad.setId(8);
		buttonLoad.setAlpha((float) 0.5);

		buttonSave = (ImageButton) findViewById(R.id.buttonsupersave);
		buttonSave.setOnTouchListener(touchListener);
		buttonSave.setId(9);

		buttonWeapon = (ImageButton) findViewById(R.id.buttonweapon);
		buttonWeapon.setId(10);
		buttonWeapon.setOnTouchListener(touchListener);

		buttonInventory = (ImageButton) findViewById(R.id.buttoninventory);
		buttonInventory.setId(11);
		buttonInventory.setOnTouchListener(touchListener);

		buttonJump = (ImageButton) findViewById(R.id.buttonsuperjump);
		buttonJump.setId(12);
		buttonJump.setOnTouchListener(touchListener);

		buttonFire = (ImageButton) findViewById(R.id.buttonFire);

		buttonFire.setOnTouchListener(touchListener);
		buttonFire.setId(13);

		buttonMagic = (ImageButton) findViewById(R.id.buttonMagic);
		buttonMagic.setId(14);
		buttonMagic.setOnTouchListener(touchListener);

		buttonUse = (ImageButton) findViewById(R.id.buttonUse);
		buttonUse.setId(15);
		buttonUse.setOnTouchListener(touchListener);

		buttonCrouch = (ImageButton) findViewById(R.id.buttoncrouch);
		buttonCrouch.setId(16);
		buttonCrouch.setOnTouchListener(touchListener);

		if (controlsFlag == -1 || controlsFlag == 1) {
			joystick.setLayoutParams(ControlsParams.coordinates(joystick, 20,
					400, 250, 250));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_JOYSTICK_SIZE,
					CoordinatesAllScreens.getInstance().getScaledCoordinateX(
							250));

			buttonRun.setLayoutParams(ControlsParams.coordinates(buttonRun, 10,
					330, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_RUN_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(70));

			buttonRun.setAlpha((float) 0.5);
			joystick.setAlpha((float) 0.5);
			buttonCrouch.setAlpha((float) 0.5);
			buttonUse.setAlpha((float) 0.5);
			buttonMagic.setAlpha((float) 0.5);
			buttonFire.setAlpha((float) 0.5);
			buttonJump.setAlpha((float) 0.5);
			buttonWeapon.setAlpha((float) 0.5);
			buttonInventory.setAlpha((float) 0.5);
			buttonLoad.setAlpha((float) 0.5);

			buttonSave.setAlpha((float) 0.5);
			buttonPause.setAlpha((float) 0.5);
			buttonDiary.setAlpha((float) 0.5);
			buttonTouch.setAlpha((float) 0.5);
			buttonChangePerson.setAlpha((float) 0.5);
			buttonWait.setAlpha((float) 0.5);
			buttonConsole.setAlpha((float) 0.5);

			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_RUN_OPACITY, (float) 0.5);

			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_CROUCH_OPACITY,
					(float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_OPACITY,
					(float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_WAIT_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_CONSOLE_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_TOUCH_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_DIARY_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_PAUSE_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_LOAD_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_SAVE_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_WEAPON_OPACITY,
					(float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_INVENTORY_OPACITY,
					(float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_JUMP_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_FIRE_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_MAGIC_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_USE_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_CROUCH_OPACITY,
					(float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_JOYSTICK_OPACITY, (float) 0.5);

			buttonConsole.setLayoutParams(ControlsParams.coordinates(
					buttonConsole, 140, 0, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_CONSOLE_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(70));

			buttonChangePerson.setLayoutParams(ControlsParams.coordinates(
					buttonChangePerson, 212, 0, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(70));
			buttonWait.setLayoutParams(ControlsParams.coordinates(buttonWait,
					274, 0, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_WAIT_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(70));
			buttonTouch.setLayoutParams(ControlsParams.coordinates(buttonTouch,
					346, 0, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_TOUCH_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(70));
			buttonWeapon.setLayoutParams(ControlsParams.coordinates(
					buttonWeapon, 880, 95, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_WEAPON_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(70));
			buttonDiary.setLayoutParams(ControlsParams.coordinates(buttonDiary,
					414, 0, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_DIARY_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(70));
			buttonPause.setLayoutParams(ControlsParams.coordinates(buttonPause,
					950, 0, 60, 60));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_PAUSE_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(60));
			buttonLoad.setLayoutParams(ControlsParams.coordinates(buttonLoad,
					880, 0, 60, 60));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_LOAD_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(60));
			buttonSave.setLayoutParams(ControlsParams.coordinates(buttonSave,
					820, 0, 60, 60));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_SAVE_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(60));
			buttonInventory.setLayoutParams(ControlsParams.coordinates(
					buttonInventory, 950, 95, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_INVENTORY_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(70));

			buttonJump.setLayoutParams(ControlsParams.coordinates(buttonJump,
					920, 195, 90, 90));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_JUMP_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(90));

			buttonFire.setLayoutParams(ControlsParams.coordinates(buttonFire,
					790, 300, 100, 100));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_FIRE_SIZE,
					CoordinatesAllScreens.getInstance().getScaledCoordinateX(
							100));

			buttonMagic.setLayoutParams(ControlsParams.coordinates(buttonMagic,
					940, 480, 80, 80));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_MAGIC_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(80));

			buttonUse.setLayoutParams(ControlsParams.coordinates(buttonUse,
					940, 368, 80, 80));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_USE_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(80));

			buttonCrouch.setLayoutParams(ControlsParams.coordinates(
					buttonCrouch, 940, 670, 80, 80));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_CROUCH_SIZE,
					CoordinatesAllScreens.getInstance()
							.getScaledCoordinateX(80));

			Editor editor = Settings.edit();
			editor.putInt(Constants.APP_PREFERENCES_RESET_CONTROLS, 0);
			editor.apply();
		} else if (controlsFlag == 0) {
			buttonRun.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_RUN_OPACITY, -1));
			buttonConsole.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_CONSOLE_OPACITY, -1));
			buttonChangePerson.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_OPACITY, -1));
			buttonWait.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_WAIT_OPACITY, -1));
			buttonTouch.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_TOUCH_OPACITY, -1));
			buttonDiary.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_DIARY_OPACITY, -1));
			buttonPause.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_PAUSE_OPACITY, -1));
			buttonLoad.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_LOAD_OPACITY, -1));
			buttonSave.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_SAVE_OPACITY, -1));
			buttonWeapon.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_WEAPON_OPACITY, -1));
			buttonInventory.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_INVENTORY_OPACITY, -1));
			buttonJump.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_JUMP_OPACITY, -1));
			buttonFire.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_FIRE_OPACITY, -1));
			buttonUse.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_USE_OPACITY, -1));
			buttonMagic.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_MAGIC_OPACITY, -1));
			buttonCrouch.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_CROUCH_OPACITY, -1));
			joystick.setAlpha(Settings.getFloat(
					Constants.APP_PREFERENCES_JOYSTICK_OPACITY, -1));

			joystick.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					joystick,
					Settings.getInt(Constants.APP_PREFERENCES_JOYSTICK_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_JOYSTICK_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_JOYSTICK_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_JOYSTICK_SIZE, -1)));
			buttonRun
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonRun,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_RUN_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_RUN_Y, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_RUN_SIZE,
									-1), Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_RUN_SIZE,
									-1)));

			buttonConsole
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonConsole,
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CONSOLE_X,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CONSOLE_Y,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CONSOLE_SIZE,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CONSOLE_SIZE,
											-1)));
			buttonChangePerson
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonChangePerson,
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_X,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_Y,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_SIZE,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_SIZE,
											-1)));
			buttonWait
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonWait,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_WAIT_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_WAIT_Y, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_WAIT_SIZE,
									-1), Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_WAIT_SIZE,
									-1)));
			buttonTouch
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonTouch,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_TOUCH_X,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_TOUCH_Y,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_TOUCH_SIZE,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_TOUCH_SIZE,
									-1)));
			buttonWeapon
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonWeapon,
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_WEAPON_X,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_WEAPON_Y,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_WEAPON_SIZE,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_WEAPON_SIZE,
											-1)));
			buttonDiary
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonDiary,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_DIARY_X,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_DIARY_Y,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_DIARY_SIZE,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_DIARY_SIZE,
									-1)));
			buttonPause
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonPause,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_PAUSE_X,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_PAUSE_Y,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_PAUSE_SIZE,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_PAUSE_SIZE,
									-1)));
			buttonLoad
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonLoad,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_LOAD_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_LOAD_Y, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_LOAD_SIZE,
									-1), Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_LOAD_SIZE,
									-1)));
			buttonSave
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonSave,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_SAVE_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_SAVE_Y, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_SAVE_SIZE,
									-1), Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_SAVE_SIZE,
									-1)));
			buttonInventory
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonInventory,
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_INVENTORY_X,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_INVENTORY_Y,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_INVENTORY_SIZE,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_INVENTORY_SIZE,
											-1)));
			buttonJump
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonJump,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_JUMP_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_JUMP_Y, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_JUMP_SIZE,
									-1), Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_JUMP_SIZE,
									-1)));
			buttonFire
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonFire,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_FIRE_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_FIRE_Y, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_FIRE_SIZE,
									-1), Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_FIRE_SIZE,
									-1)));
			buttonMagic
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonMagic,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_MAGIC_X,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_MAGIC_Y,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_MAGIC_SIZE,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_MAGIC_SIZE,
									-1)));
			buttonUse
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonUse,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_USE_X, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_USE_Y, -1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_USE_SIZE,
									-1), Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_USE_SIZE,
									-1)));
			buttonCrouch
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonCrouch,
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CROUCH_X,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CROUCH_Y,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CROUCH_SIZE,
											-1),
									Settings.getInt(
											Constants.APP_PREFERENCES_BUTTON_CROUCH_SIZE,
											-1)));

		}

	}

	@Override
	public void onDestroy() {

		buttonFlag = 0;
		super.onDestroy();
		setSharedPreferences();

	}

	@Override
	public void onPause() {

		super.onPause();
		setSharedPreferences();

	}

	public void setSharedPreferences() {
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

	public float setAlphaFromButtonToSharedPreferences(String name) {
		float opacity;
		opacity = Settings.getFloat(name, -1);
		if (opacity != 0.1 && buttonOpacityFlag == false)
			opacity = opacity - (float) 0.1;
		else if (opacity != 1.0 && buttonOpacityFlag == true)
			opacity = opacity + (float) 0.1;
		Editor editor = Settings.edit();
		editor.putFloat(name, opacity);
		editor.apply();
		return opacity;
	}

	public int setSizeFromButtonToSharedPreferences(String name) {
		int size;
		size = Settings.getInt(name, -1);
		if (size > 10 && buttonSizeFlag == false)
			size = size - 10;
		else if (buttonSizeFlag == true)
			size = size + 10;
		Editor editor = Settings.edit();
		editor.putInt(name, size);
		editor.apply();
		return size;
	}

	public void setAlphaToSharedPreferences(String name, float opacity) {

		Editor editor = Settings.edit();
		editor.putFloat(name, opacity);
		editor.apply();
	}

	public void setSizeToSharedPreferences(String name, int size) {

		Editor editor = Settings.edit();
		editor.putInt(name, size);
		editor.apply();
	}

	public void setButtonsOpacity() {
		if (buttonFlag == 1)
			buttonRun
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_RUN_OPACITY));
		else if (buttonFlag == 2)
			buttonConsole
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CONSOLE_OPACITY));
		else if (buttonFlag == 3)
			buttonChangePerson
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_OPACITY));
		else if (buttonFlag == 4)
			buttonWait
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WAIT_OPACITY));
		else if (buttonFlag == 5)
			buttonTouch
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_TOUCH_OPACITY));
		else if (buttonFlag == 6)
			buttonDiary
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_DIARY_OPACITY));
		else if (buttonFlag == 7)
			buttonPause
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_PAUSE_OPACITY));
		if (buttonFlag == 8)
			buttonLoad
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_LOAD_OPACITY));
		else if (buttonFlag == 9)
			buttonSave
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_SAVE_OPACITY));
		else if (buttonFlag == 10)
			buttonWeapon
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WEAPON_OPACITY));
		else if (buttonFlag == 11)
			buttonInventory
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_INVENTORY_OPACITY));
		else if (buttonFlag == 12)
			buttonJump
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_JUMP_OPACITY));
		else if (buttonFlag == 13)
			buttonFire
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_FIRE_OPACITY));
		else if (buttonFlag == 14)
			buttonMagic
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_MAGIC_OPACITY));
		else if (buttonFlag == 15)
			buttonUse
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_USE_OPACITY));
		else if (buttonFlag == 16)
			buttonCrouch
					.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CROUCH_OPACITY));
		else if (buttonFlag == 17)
			joystick.setAlpha(setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_JOYSTICK_OPACITY));

	}

	public void setButtonsSize() {
		if (buttonFlag == 1)
			buttonRun
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonRun,
							(int) buttonRun.getX(),
							(int) buttonRun.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_RUN_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_RUN_SIZE)));
		else if (buttonFlag == 2)
			buttonConsole
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonConsole,
									(int) buttonConsole.getX(),
									(int) buttonConsole.getY(),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CONSOLE_SIZE),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CONSOLE_SIZE)));

		else if (buttonFlag == 3)
			buttonChangePerson
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonChangePerson,
									(int) buttonChangePerson.getX(),
									(int) buttonChangePerson.getY(),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_SIZE),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_SIZE)));
		else if (buttonFlag == 4)
			buttonWait
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonWait,
							(int) buttonWait.getX(),
							(int) buttonWait.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WAIT_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WAIT_SIZE)));
		else if (buttonFlag == 5)
			buttonTouch
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonTouch,
							(int) buttonTouch.getX(),
							(int) buttonTouch.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_TOUCH_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_TOUCH_SIZE)));
		else if (buttonFlag == 6)
			buttonDiary
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonDiary,
							(int) buttonDiary.getX(),
							(int) buttonDiary.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_DIARY_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_DIARY_SIZE)));
		else if (buttonFlag == 7)
			buttonPause
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonPause,
							(int) buttonPause.getX(),
							(int) buttonPause.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_PAUSE_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_PAUSE_SIZE)));

		if (buttonFlag == 8)
			buttonLoad
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonLoad,
							(int) buttonLoad.getX(),
							(int) buttonLoad.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_LOAD_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_LOAD_SIZE)));

		else if (buttonFlag == 9)
			buttonSave
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonSave,
							(int) buttonSave.getX(),
							(int) buttonSave.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_SAVE_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_SAVE_SIZE)));

		else if (buttonFlag == 10)
			buttonWeapon
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonWeapon,
									(int) buttonWeapon.getX(),
									(int) buttonWeapon.getY(),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WEAPON_SIZE),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WEAPON_SIZE)));

		else if (buttonFlag == 11)
			buttonInventory
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonInventory,
									(int) buttonInventory.getX(),
									(int) buttonInventory.getY(),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_INVENTORY_SIZE),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_INVENTORY_SIZE)));

		else if (buttonFlag == 12)
			buttonJump
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonJump,
							(int) buttonJump.getX(),
							(int) buttonJump.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_JUMP_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_JUMP_SIZE)));

		else if (buttonFlag == 13)
			buttonFire
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonFire,
							(int) buttonFire.getX(),
							(int) buttonFire.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_FIRE_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_FIRE_SIZE)));

		else if (buttonFlag == 14)
			buttonMagic
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonMagic,
							(int) buttonMagic.getX(),
							(int) buttonMagic.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_MAGIC_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_MAGIC_SIZE)));
		else if (buttonFlag == 15)
			buttonUse
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonUse,
							(int) buttonUse.getX(),
							(int) buttonUse.getY(),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_USE_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_USE_SIZE)));

		else if (buttonFlag == 16)
			buttonCrouch
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonCrouch,
									(int) buttonCrouch.getX(),
									(int) buttonCrouch.getY(),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CROUCH_SIZE),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CROUCH_SIZE)));
		else if (buttonFlag == 17)
			joystick.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					joystick,
					(int) joystick.getX(),
					(int) joystick.getY(),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_JOYSTICK_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_JOYSTICK_SIZE)));

	}
}

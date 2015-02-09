package com.libopenmw.openmw;

import java.io.File;

import org.libsdl.app.SDLActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class GameActivity extends SDLActivity {

	public static native void getPathToJni(String path);

	// Load the .so
	static {

		System.loadLibrary("openal");
		System.loadLibrary("openmw");

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getPathToJni(MainActivity.configsPath);

		File inputfile = new File(MainActivity.dataPath
				+ "/Video/bethesda logo.bik");
		if (inputfile.exists())
			inputfile.delete();

		if (MainActivity.contols == true) {

			int controlsFlag;

			SharedPreferences Settings;

			Settings = getSharedPreferences(Constants.APP_PREFERENCES,
					Context.MODE_MULTI_PROCESS);
			controlsFlag = Settings.getInt(
					Constants.APP_PREFERENCES_RESET_CONTROLS, -1);
			enableTouch = false;

			DisplayMetrics displaymetrics;
			displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			CoordinatesAllScreens.height = displaymetrics.heightPixels;
			CoordinatesAllScreens.width = displaymetrics.widthPixels;
			LayoutInflater inflater = getLayoutInflater();
			getWindow().addContentView(
					inflater.inflate(R.layout.screencontrols, null),
					new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.FILL_PARENT,
							ViewGroup.LayoutParams.FILL_PARENT));

			final Controls joystick = (Controls) findViewById(R.id.joystick);

			final ImageButton buttonRun = (ImageButton) findViewById(R.id.buttonrun1);

			buttonRun.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_CAPS_LOCK));

			final ImageButton buttonConsole = (ImageButton) findViewById(R.id.buttonconsole);

			buttonConsole.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_F2));

			final ImageButton buttonChangePerson = (ImageButton) findViewById(R.id.buttonchangeperson);

			buttonChangePerson.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_TAB));

			final ImageButton buttonWait = (ImageButton) findViewById(R.id.buttonwait);

			buttonWait.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_T));

			final Button buttonTouch = (Button) findViewById(R.id.buttontouch);
			final TouchCamera touch = (TouchCamera) findViewById(R.id.superTouch);

			buttonTouch.setText("off");

			buttonTouch.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						return true;
					case MotionEvent.ACTION_UP:
						if (enableTouch == false) {
							buttonTouch.setText("on");
							enableTouch = true;
							touch.setVisibility(TouchCamera.VISIBLE);

						} else {
							enableTouch = false;
							touch.setVisibility(TouchCamera.INVISIBLE);
							buttonTouch.setText("off");
						}
						return true;
					}
					return false;
				}
			});

			final ImageButton buttonLoad = (ImageButton) findViewById(R.id.buttonsuperload);

			buttonLoad.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_F9));

			final ImageButton buttonSave = (ImageButton) findViewById(R.id.buttonsupersave);

			buttonSave.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_F5));

			final ImageButton buttonWeapon = (ImageButton) findViewById(R.id.buttonweapon);

			buttonWeapon.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_F));

			final ImageButton buttonInventory = (ImageButton) findViewById(R.id.buttoninventory);

			buttonInventory.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_L));

			final ImageButton buttonJump = (ImageButton) findViewById(R.id.buttonsuperjump);

			buttonJump.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_E));

			final ImageButton buttonFire = (ImageButton) findViewById(R.id.buttonFire);

			buttonFire.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_Z));
			final ImageButton buttonMagic = (ImageButton) findViewById(R.id.buttonMagic);

			buttonMagic.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_R));
			crouchFlag = false;

			final ImageButton buttonCrouch = (ImageButton) findViewById(R.id.buttoncrouch);

			buttonCrouch.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						if (crouchFlag == false) {
							SDLActivity
									.onNativeKeyDown(KeyEvent.KEYCODE_CTRL_LEFT);
							crouchFlag = true;
						} else {
							SDLActivity
									.onNativeKeyUp(KeyEvent.KEYCODE_CTRL_LEFT);
							crouchFlag = false;
						}
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonDiary = (ImageButton) findViewById(R.id.buttonDiary);

			buttonDiary.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_J));

			final ImageButton buttonUse = (ImageButton) findViewById(R.id.buttonUse);

			buttonUse.setOnTouchListener(new ButtonTouchListener(
					KeyEvent.KEYCODE_SPACE));
			hideControls = false;
			final ImageButton buttonPause = (ImageButton) findViewById(R.id.buttonpause);

			buttonPause.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						if (hideControls == false) {
							enableTouch = false;
							touch.setVisibility(TouchCamera.INVISIBLE);
							buttonTouch.setText("off");
							buttonChangePerson.setVisibility(ImageButton.GONE);
							joystick.setVisibility(JoystickView.GONE);
							buttonConsole.setVisibility(ImageButton.GONE);
							buttonTouch.setVisibility(Button.GONE);
							buttonConsole.setVisibility(ImageButton.GONE);
							buttonCrouch.setVisibility(ImageButton.GONE);
							buttonDiary.setVisibility(ImageButton.GONE);
							buttonFire.setVisibility(ImageButton.GONE);
							buttonInventory.setVisibility(ImageButton.GONE);
							buttonJump.setVisibility(ImageButton.GONE);
							buttonLoad.setVisibility(ImageButton.GONE);
							buttonMagic.setVisibility(ImageButton.GONE);
							buttonRun.setVisibility(ImageButton.GONE);
							buttonSave.setVisibility(ImageButton.GONE);
							buttonUse.setVisibility(ImageButton.GONE);
							buttonWait.setVisibility(ImageButton.GONE);
							buttonWeapon.setVisibility(ImageButton.GONE);

							hideControls = true;
						} else {

							buttonChangePerson
									.setVisibility(ImageButton.VISIBLE);
							joystick.setVisibility(JoystickView.VISIBLE);
							buttonConsole.setVisibility(ImageButton.VISIBLE);
							buttonTouch.setVisibility(Button.VISIBLE);
							buttonConsole.setVisibility(ImageButton.VISIBLE);
							buttonCrouch.setVisibility(ImageButton.VISIBLE);
							buttonDiary.setVisibility(ImageButton.VISIBLE);
							buttonFire.setVisibility(ImageButton.VISIBLE);
							buttonInventory.setVisibility(ImageButton.VISIBLE);
							buttonJump.setVisibility(ImageButton.VISIBLE);
							buttonLoad.setVisibility(ImageButton.VISIBLE);
							buttonMagic.setVisibility(ImageButton.VISIBLE);
							buttonRun.setVisibility(ImageButton.VISIBLE);
							buttonSave.setVisibility(ImageButton.VISIBLE);
							buttonUse.setVisibility(ImageButton.VISIBLE);
							buttonWait.setVisibility(ImageButton.VISIBLE);
							buttonWeapon.setVisibility(ImageButton.VISIBLE);

							hideControls = false;
						}
						// SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_ESCAPE);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						// SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_ESCAPE);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			if (controlsFlag == -1 || controlsFlag == 1) {
				joystick.setLayoutParams(ControlsParams.coordinates(joystick,
						20, 400, 250, 250));

				buttonRun.setLayoutParams(ControlsParams.coordinates(buttonRun,
						10, 330, 70, 70));

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

				buttonConsole.setLayoutParams(ControlsParams.coordinates(
						buttonConsole, 140, 0, 70, 70));

				buttonChangePerson.setLayoutParams(ControlsParams.coordinates(
						buttonChangePerson, 212, 0, 70, 70));
				buttonWait.setLayoutParams(ControlsParams.coordinates(
						buttonWait, 274, 0, 70, 70));
				buttonTouch.setLayoutParams(ControlsParams.coordinates(
						buttonTouch, 346, 0, 70, 70));
				buttonWeapon.setLayoutParams(ControlsParams.coordinates(
						buttonWeapon, 880, 95, 70, 70));
				buttonDiary.setLayoutParams(ControlsParams.coordinates(
						buttonDiary, 414, 0, 70, 70));
				buttonPause.setLayoutParams(ControlsParams.coordinates(
						buttonPause, 950, 0, 60, 60));
				buttonLoad.setLayoutParams(ControlsParams.coordinates(
						buttonLoad, 880, 0, 60, 60));
				buttonSave.setLayoutParams(ControlsParams.coordinates(
						buttonSave, 820, 0, 60, 60));
				buttonInventory.setLayoutParams(ControlsParams.coordinates(
						buttonInventory, 950, 95, 70, 70));

				buttonJump.setLayoutParams(ControlsParams.coordinates(
						buttonJump, 920, 195, 90, 90));

				buttonFire.setLayoutParams(ControlsParams.coordinates(
						buttonFire, 790, 300, 100, 100));

				buttonMagic.setLayoutParams(ControlsParams.coordinates(
						buttonMagic, 940, 480, 80, 80));

				buttonUse.setLayoutParams(ControlsParams.coordinates(buttonUse,
						940, 368, 80, 80));

				buttonCrouch.setLayoutParams(ControlsParams.coordinates(
						buttonCrouch, 940, 670, 80, 80));

			} else if (controlsFlag == 0) {
				buttonRun.setAlpha(Settings.getFloat(
						Constants.APP_PREFERENCES_BUTTON_RUN_OPACITY, -1));
				buttonConsole.setAlpha(Settings.getFloat(
						Constants.APP_PREFERENCES_BUTTON_CONSOLE_OPACITY, -1));
				buttonChangePerson.setAlpha(Settings.getFloat(
						Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_OPACITY,
						-1));
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
				buttonInventory
						.setAlpha(Settings
								.getFloat(
										Constants.APP_PREFERENCES_BUTTON_INVENTORY_OPACITY,
										-1));
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
						joystick, Settings.getInt(
								Constants.APP_PREFERENCES_JOYSTICK_X, -1),
						Settings.getInt(Constants.APP_PREFERENCES_JOYSTICK_Y,
								-1), Settings.getInt(
								Constants.APP_PREFERENCES_JOYSTICK_SIZE, -1),
						Settings.getInt(
								Constants.APP_PREFERENCES_JOYSTICK_SIZE, -1)));
				buttonRun
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
										buttonRun,
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_RUN_X,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_RUN_Y,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_RUN_SIZE,
												-1),
										Settings.getInt(
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
						.setLayoutParams(ControlsParams.coordinatesConfigureControls(
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
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
										buttonWait,
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_WAIT_X,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_WAIT_Y,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_WAIT_SIZE,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_WAIT_SIZE,
												-1)));
				buttonTouch
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
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
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
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
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
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
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
										buttonLoad,
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_LOAD_X,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_LOAD_Y,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_LOAD_SIZE,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_LOAD_SIZE,
												-1)));
				buttonSave
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
										buttonSave,
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_SAVE_X,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_SAVE_Y,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_SAVE_SIZE,
												-1),
										Settings.getInt(
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
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
										buttonJump,
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_JUMP_X,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_JUMP_Y,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_JUMP_SIZE,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_JUMP_SIZE,
												-1)));
				buttonFire
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
										buttonFire,
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_FIRE_X,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_FIRE_Y,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_FIRE_SIZE,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_FIRE_SIZE,
												-1)));
				buttonMagic
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
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
						.setLayoutParams(ControlsParams
								.coordinatesConfigureControls(
										buttonUse,
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_USE_X,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_USE_Y,
												-1),
										Settings.getInt(
												Constants.APP_PREFERENCES_BUTTON_USE_SIZE,
												-1),
										Settings.getInt(
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

	}
	@Override
	public void onDestroy() {
		finish();
		Process.killProcess(Process.myPid());

		super.onDestroy();
	}
	
	
}

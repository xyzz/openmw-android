package ui.activity;

import com.libopenmw.openmw.R;

import screen.ScreenScaler;
import ui.controls.AlphaView;
import ui.controls.ControlsParams;
import ui.controls.Joystick;
import ui.controls.MultiTouchListener;
import constants.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import com.nineoldandroids.view.ViewHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ConfigureControls extends Activity {

	public Context context;

	private Joystick joystick;
	private ImageButton buttonRun;
	private ImageButton buttonConsole;
	private ImageButton buttonChangePerson;
	private ImageButton buttonWait;
	private Button buttonSize;
	private Button buttonSize1;
	private Button buttonTouch;

	private Button buttonOpacity;
	private Button buttonOpacity1;

	public static TextView button;

	private ImageButton buttonDiary;
	private ImageButton buttonPause;
	private ImageButton buttonLoad;
	private ImageButton buttonSave;
	private ImageButton buttonWeapon;
	private ImageButton buttonInventory;
	private ImageButton buttonJump;
	private ImageButton buttonFire;
	private ImageButton buttonMagic;
	private ImageButton buttonUse;
	private ImageButton buttonCrouch;
	Button f1;
	Button showPanel;
	ImageButton key0;
	ImageButton key1;
	ImageButton key2;
	ImageButton key3;
	ImageButton key4;
	ImageButton key5;
	ImageButton key6;
	ImageButton key7;
	ImageButton key8;
	ImageButton key9;
	public static int buttonFlag = 0;

	public static boolean buttonOpacityFlag = false;
	public static boolean buttonSizeFlag = false;
	private SharedPreferences Settings;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.screencontrols);

		int controlsFlag;

		context = this;

		Settings = getSharedPreferences(Constants.APP_PREFERENCES,
				Context.MODE_PRIVATE);
		controlsFlag = Settings.getInt(
				Constants.APP_PREFERENCES_RESET_CONTROLS, -1);
		DisplayMetrics displaymetrics;
		displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		ScreenScaler.height = displaymetrics.heightPixels;
		ScreenScaler.width = displaymetrics.widthPixels;

		final MultiTouchListener touchListener = new MultiTouchListener();

		f1 = (Button) findViewById(R.id.F1);
		showPanel = (Button) findViewById(R.id.showQuickPanel);
		key0 = (ImageButton) findViewById(R.id.key0);
		key1 = (ImageButton) findViewById(R.id.key1);
		key2 = (ImageButton) findViewById(R.id.key2);
		key3 = (ImageButton) findViewById(R.id.key3);
		key4 = (ImageButton) findViewById(R.id.key4);
		key5 = (ImageButton) findViewById(R.id.key5);
		key6 = (ImageButton) findViewById(R.id.key6);
		key7 = (ImageButton) findViewById(R.id.key7);
		key8 = (ImageButton) findViewById(R.id.key8);
		key9 = (ImageButton) findViewById(R.id.key9);

		f1.setOnTouchListener(touchListener);
		f1.setId(18);
		showPanel.setOnTouchListener(touchListener);
		showPanel.setId(19);
		key0.setOnTouchListener(touchListener);
		key0.setId(20);
		key1.setOnTouchListener(touchListener);
		key1.setId(21);
		key2.setOnTouchListener(touchListener);
		key2.setId(22);
		key3.setOnTouchListener(touchListener);
		key3.setId(23);
		key4.setOnTouchListener(touchListener);
		key4.setId(24);
		key5.setOnTouchListener(touchListener);
		key5.setId(25);
		key6.setOnTouchListener(touchListener);
		key6.setId(26);
		key7.setOnTouchListener(touchListener);
		key7.setId(27);
		key8.setOnTouchListener(touchListener);
		key8.setId(28);
		key9.setOnTouchListener(touchListener);
		key9.setId(29);

		buttonSize = (Button) findViewById(R.id.buttonsize1);
		buttonSize1 = (Button) findViewById(R.id.buttonsize2);
		button = (TextView) findViewById(R.id.textViewTitle);

		buttonSize.setLayoutParams(ControlsParams.coordinates(buttonSize, 400,
				280, 100, 80));
		buttonSize.setVisibility(Button.VISIBLE);
		AlphaView.setAlphaForView(buttonSize, 0.5f);

		buttonSize1.setLayoutParams(ControlsParams.coordinates(buttonSize1,
				520, 280, 100, 80));
		AlphaView.setAlphaForView(buttonSize1, 0.5f);

		buttonSize1.setVisibility(Button.VISIBLE);

		buttonOpacity = (Button) findViewById(R.id.buttonopacity1);
		buttonOpacity1 = (Button) findViewById(R.id.buttonopacity2);

		buttonOpacity.setLayoutParams(ControlsParams.coordinates(buttonOpacity,
				400, 400, 120, 100));
		buttonOpacity.setVisibility(Button.VISIBLE);

		buttonOpacity1.setLayoutParams(ControlsParams.coordinates(
				buttonOpacity1, 540, 400, 120, 100));
		buttonOpacity1.setVisibility(Button.VISIBLE);
		AlphaView.setAlphaForView(buttonOpacity, 0.5f);
		AlphaView.setAlphaForView(buttonOpacity1, 0.5f);

		button.setTextColor(Color.WHITE);
		button.setTextSize((float) ScreenScaler.getInstance()
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

		joystick = (Joystick) findViewById(R.id.joystick);
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
			joystick.setLayoutParams(ControlsParams.coordinates(joystick, 75,
					400, 250, 250));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_JOYSTICK_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(250));

			buttonRun.setLayoutParams(ControlsParams.coordinates(buttonRun, 65,
					330, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_RUN_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(70));

			AlphaView.setAlphaForView(buttonRun, 0.5f);
			AlphaView.setAlphaForView(joystick,0.5f);
			AlphaView.setAlphaForView(buttonCrouch,0.5f);
			AlphaView.setAlphaForView(buttonUse,0.5f);
			AlphaView.setAlphaForView(buttonMagic,0.5f);
			AlphaView.setAlphaForView(buttonFire,0.5f);
			AlphaView.setAlphaForView(buttonJump,0.5f);
			AlphaView.setAlphaForView(buttonJump,0.5f);
			AlphaView.setAlphaForView(buttonWeapon,0.5f);
			AlphaView.setAlphaForView(buttonInventory,0.5f);
			AlphaView.setAlphaForView(buttonLoad,0.5f);
			AlphaView.setAlphaForView(buttonSave,0.5f);
			AlphaView.setAlphaForView(buttonPause,0.5f);
			AlphaView.setAlphaForView(buttonDiary,0.5f);
			AlphaView.setAlphaForView(buttonTouch,0.5f);
			AlphaView.setAlphaForView(buttonChangePerson,0.5f);
			AlphaView.setAlphaForView(buttonWait,0.5f);
			AlphaView.setAlphaForView(buttonConsole,0.5f);

			showPanel.setLayoutParams(ControlsParams.coordinates(showPanel, 68,
					0, 65, 65));
			f1.setLayoutParams(ControlsParams.coordinates(f1, 68, 95, 55, 55));
			key0.setLayoutParams(ControlsParams.coordinates(key0, 0, 0, 55, 55));
			key1.setLayoutParams(ControlsParams
					.coordinates(key1, 0, 75, 55, 55));
			key2.setLayoutParams(ControlsParams.coordinates(key2, 0, 150, 55,
					55));
			key3.setLayoutParams(ControlsParams.coordinates(key3, 0, 225, 55,
					55));
			key4.setLayoutParams(ControlsParams.coordinates(key4, 0, 300, 55,
					55));
			key5.setLayoutParams(ControlsParams.coordinates(key5, 0, 375, 55,
					55));
			key6.setLayoutParams(ControlsParams.coordinates(key6, 0, 450, 55,
					55));
			key7.setLayoutParams(ControlsParams.coordinates(key7, 0, 525, 55,
					55));
			key8.setLayoutParams(ControlsParams.coordinates(key8, 0, 600, 55,
					55));
			key9.setLayoutParams(ControlsParams.coordinates(key9, 0, 675, 55,
					55));

			AlphaView.setAlphaForView(showPanel, 0.5f);
			AlphaView.setAlphaForView(f1, 0.5f);
			AlphaView.setAlphaForView(key0, 1.0f);
			AlphaView.setAlphaForView(key1, 1.0f);
			AlphaView.setAlphaForView(key2, 1.0f);
			AlphaView.setAlphaForView(key3, 1.0f);
			AlphaView.setAlphaForView(key5, 1.0f);
			AlphaView.setAlphaForView(key6, 1.0f);
			AlphaView.setAlphaForView(key7, 1.0f);
			AlphaView.setAlphaForView(key8, 1.0f);
			AlphaView.setAlphaForView(key9, 1.0f);


			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_OPASITY,
					(float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_F1_OPACITY, (float) 0.5);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_0_OPACITY, (float) 1.0);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_1_OPACITY, (float) 1.0);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_2_OPACITY, (float) 1.0);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_3_OPACITY, (float) 1.0);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_4_OPACITY, (float) 1.0);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_5_OPACITY, (float) 1.0);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_6_OPACITY, (float) 1.0);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_7_OPACITY, (float) 1.0);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_8_OPACITY, (float) 1.0);
			setAlphaToSharedPreferences(
					Constants.APP_PREFERENCES_KEY_9_OPACITY, (float) 1.0);

			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(65));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_F1_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_0_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_1_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_2_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_3_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_4_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_5_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_6_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_7_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_8_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));
			setSizeToSharedPreferences(Constants.APP_PREFERENCES_KEY_9_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(55));

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
					Constants.APP_PREFERENCES_BUTTON_CONSOLE_OPACITY,
					(float) 0.5);
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
					Constants.APP_PREFERENCES_BUTTON_CONSOLE_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(70));

			buttonChangePerson.setLayoutParams(ControlsParams.coordinates(
					buttonChangePerson, 212, 0, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(70));
			buttonWait.setLayoutParams(ControlsParams.coordinates(buttonWait,
					274, 0, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_WAIT_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(70));
			buttonTouch.setLayoutParams(ControlsParams.coordinates(buttonTouch,
					346, 0, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_TOUCH_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(70));
			buttonWeapon.setLayoutParams(ControlsParams.coordinates(
					buttonWeapon, 880, 95, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_WEAPON_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(70));
			buttonDiary.setLayoutParams(ControlsParams.coordinates(buttonDiary,
					414, 0, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_DIARY_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(70));
			buttonPause.setLayoutParams(ControlsParams.coordinates(buttonPause,
					950, 0, 60, 60));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_PAUSE_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(60));
			buttonLoad.setLayoutParams(ControlsParams.coordinates(buttonLoad,
					880, 0, 60, 60));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_LOAD_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(60));
			buttonSave.setLayoutParams(ControlsParams.coordinates(buttonSave,
					820, 0, 60, 60));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_SAVE_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(60));
			buttonInventory.setLayoutParams(ControlsParams.coordinates(
					buttonInventory, 950, 95, 70, 70));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_INVENTORY_SIZE,
					ScreenScaler.getInstance().getScaledCoordinateX(70));

			buttonJump.setLayoutParams(ControlsParams.coordinates(buttonJump,
					920, 195, 90, 90));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_JUMP_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(90));

			buttonFire.setLayoutParams(ControlsParams.coordinates(buttonFire,
					790, 300, 100, 100));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_FIRE_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(100));

			buttonMagic.setLayoutParams(ControlsParams.coordinates(buttonMagic,
					940, 480, 80, 80));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_MAGIC_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(80));

			buttonUse.setLayoutParams(ControlsParams.coordinates(buttonUse,
					940, 368, 80, 80));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_USE_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(80));

			buttonCrouch.setLayoutParams(ControlsParams.coordinates(
					buttonCrouch, 940, 670, 80, 80));
			setSizeToSharedPreferences(
					Constants.APP_PREFERENCES_BUTTON_CROUCH_SIZE, ScreenScaler
							.getInstance().getScaledCoordinateX(80));

			Editor editor = Settings.edit();
			editor.putInt(Constants.APP_PREFERENCES_RESET_CONTROLS, 0);
			editor.commit();
		} else if (controlsFlag == 0) {

			AlphaView.setAlphaForView(buttonRun,Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_RUN_OPACITY, 0.5f));

			AlphaView.setAlphaForView(buttonConsole,Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_CONSOLE_OPACITY, -1));

			AlphaView.setAlphaForView(buttonWait, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_WAIT_OPACITY, -1));

			AlphaView.setAlphaForView(buttonTouch, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_TOUCH_OPACITY, -1));

			AlphaView.setAlphaForView(buttonDiary, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_DIARY_OPACITY, -1));

			AlphaView.setAlphaForView(buttonPause,Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_PAUSE_OPACITY, -1));

			AlphaView.setAlphaForView(buttonLoad, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_LOAD_OPACITY, -1));

			AlphaView.setAlphaForView(buttonSave, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_SAVE_OPACITY, -1));

			AlphaView.setAlphaForView(buttonWeapon, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_WEAPON_OPACITY, -1));

			AlphaView.setAlphaForView(buttonJump, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_JUMP_OPACITY, -1));

			AlphaView.setAlphaForView(buttonInventory, Settings
					.getFloat(
							Constants.APP_PREFERENCES_BUTTON_INVENTORY_OPACITY,
							-1));

			AlphaView.setAlphaForView(buttonFire, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_FIRE_OPACITY, -1));

			AlphaView.setAlphaForView(buttonUse, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_USE_OPACITY, -1));

			AlphaView.setAlphaForView(buttonMagic, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_USE_OPACITY, -1));

			AlphaView.setAlphaForView(buttonCrouch, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_CROUCH_OPACITY, -1));

			AlphaView.setAlphaForView(joystick, Settings.getFloat(
					Constants.APP_PREFERENCES_JOYSTICK_OPACITY, -1));
			AlphaView.setAlphaForView(buttonChangePerson, Settings.getFloat(
					Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_OPACITY, -1));


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


            AlphaView.setAlphaForView(f1, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_F1_OPACITY), 0.5f));

            AlphaView.setAlphaForView(showPanel, Settings
                    .getFloat(
                            (Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_OPASITY),
                            0.5f));

            AlphaView.setAlphaForView(key0, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_0_OPACITY), 1.0f));

            AlphaView.setAlphaForView(key1, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_1_OPACITY), 1.0f));
            AlphaView.setAlphaForView(key2, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_2_OPACITY), 1.0f));
            AlphaView.setAlphaForView(key3, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_3_OPACITY), 1.0f));

            AlphaView.setAlphaForView(key4, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_4_OPACITY), 1.0f));

            AlphaView.setAlphaForView(key5, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_5_OPACITY), 1.0f));

            AlphaView.setAlphaForView(key6, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_6_OPACITY), 1.0f));
            AlphaView.setAlphaForView(key7, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_7_OPACITY), 1.0f));
            AlphaView.setAlphaForView(key8, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_8_OPACITY), 1.0f));

            AlphaView.setAlphaForView(key9, Settings.getFloat(
                    (Constants.APP_PREFERENCES_KEY_9_OPACITY), 1.0f));

			f1.setLayoutParams(ControlsParams.coordinatesConfigureControls(f1,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_F1_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_F1_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_F1_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_F1_SIZE, -1)));

			showPanel
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							showPanel,
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_X,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_Y,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_SIZE,
									-1),
							Settings.getInt(
									Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_SIZE,
									-1)));

			key0.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key0,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_0_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_0_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_0_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_0_SIZE, -1)));

			key1.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key1,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_1_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_1_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_1_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_1_SIZE, -1)));

			key2.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key2,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_2_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_2_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_2_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_2_SIZE, -1)));

			key3.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key3,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_3_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_3_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_3_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_3_SIZE, -1)));

			key4.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key4,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_4_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_4_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_4_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_4_SIZE, -1)));

			key5.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key5,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_5_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_5_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_5_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_5_SIZE, -1)));

			key6.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key6,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_6_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_6_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_6_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_6_SIZE, -1)));

			key7.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key7,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_7_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_7_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_7_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_7_SIZE, -1)));

			key8.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key8,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_8_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_8_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_8_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_8_SIZE, -1)));

			key9.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key9,
					Settings.getInt(Constants.APP_PREFERENCES_KEY_9_X, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_9_Y, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_9_SIZE, -1),
					Settings.getInt(Constants.APP_PREFERENCES_KEY_9_SIZE, -1)));

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
				(int)ViewHelper.getX(buttonRun));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_RUN_Y,
				(int) ViewHelper.getY(buttonRun));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CONSOLE_X,
				(int)ViewHelper.getX(buttonConsole));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CONSOLE_Y,
				(int) ViewHelper.getY(buttonConsole));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_X,
				(int)ViewHelper.getX(buttonChangePerson));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_Y,
				(int)ViewHelper.getY(buttonChangePerson));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_WAIT_X,
				(int) ViewHelper.getX(buttonWait));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_WAIT_Y,
				(int) ViewHelper.getY(buttonWait));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_TOUCH_X,
				(int) ViewHelper.getX(buttonTouch));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_TOUCH_Y,
				(int) ViewHelper.getY(buttonTouch));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_DIARY_X,
				(int) ViewHelper.getX(buttonDiary));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_DIARY_Y,
				(int) ViewHelper.getY(buttonDiary));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_SAVE_X,
				(int) ViewHelper.getX(buttonSave));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_SAVE_Y,
				(int) ViewHelper.getY(buttonSave));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_LOAD_X,
				(int)ViewHelper.getX(buttonLoad));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_LOAD_Y,
				(int) ViewHelper.getY(buttonLoad));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_PAUSE_X,
				(int) ViewHelper.getX(buttonPause));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_PAUSE_Y,
				(int) ViewHelper.getY(buttonPause));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_WEAPON_X,
				(int) ViewHelper.getX(buttonWeapon));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_WEAPON_Y,
				(int) ViewHelper.getY(buttonWeapon));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_INVENTORY_X,
				(int) ViewHelper.getX(buttonInventory));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_INVENTORY_Y,
				(int)ViewHelper.getY(buttonInventory));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_JUMP_X,
				(int) ViewHelper.getX(buttonJump));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_JUMP_Y,
				(int) ViewHelper.getY(buttonJump));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_FIRE_X,
				(int)ViewHelper.getX(buttonFire));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_FIRE_Y,
				(int) ViewHelper.getY(buttonFire));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_USE_X,
				(int) ViewHelper.getX(buttonUse));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_USE_Y,
				(int) ViewHelper.getY(buttonUse));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_MAGIC_X,
				(int)ViewHelper.getX( buttonMagic));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_MAGIC_Y,
				(int)ViewHelper.getY(buttonMagic));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CROUCH_X,
				(int) ViewHelper.getX(buttonCrouch));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_CROUCH_Y,
				(int) ViewHelper.getY(buttonCrouch));
		editor.putInt(Constants.APP_PREFERENCES_JOYSTICK_X,
				(int) ViewHelper.getX(joystick));
		editor.putInt(Constants.APP_PREFERENCES_JOYSTICK_Y,
				(int) ViewHelper.getY(joystick));
		editor.putInt(Constants.APP_PREFERENCES_KEY_F1_X, (int) ViewHelper.getY(f1));
		editor.putInt(Constants.APP_PREFERENCES_KEY_F1_Y, (int) ViewHelper.getY(f1));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_X,
				(int) ViewHelper.getX(showPanel));
		editor.putInt(Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_Y,
				(int) ViewHelper.getY(showPanel));

        editor.putInt(Constants.APP_PREFERENCES_KEY_0_X, (int) ViewHelper.getX(key0));
        editor.putInt(Constants.APP_PREFERENCES_KEY_0_Y, (int) ViewHelper.getY(key0));

        editor.putInt(Constants.APP_PREFERENCES_KEY_1_X, (int) ViewHelper.getX(key1));
        editor.putInt(Constants.APP_PREFERENCES_KEY_1_Y, (int) ViewHelper.getY(key1));
        editor.putInt(Constants.APP_PREFERENCES_KEY_2_X, (int) ViewHelper.getX(key2));
        editor.putInt(Constants.APP_PREFERENCES_KEY_2_Y, (int) ViewHelper.getY(key2));
        editor.putInt(Constants.APP_PREFERENCES_KEY_3_X, (int) ViewHelper.getX(key3));
        editor.putInt(Constants.APP_PREFERENCES_KEY_3_Y, (int) ViewHelper.getY(key3));
        editor.putInt(Constants.APP_PREFERENCES_KEY_4_X, (int) ViewHelper.getX(key4));
        editor.putInt(Constants.APP_PREFERENCES_KEY_4_Y, (int) ViewHelper.getY(key4));
        editor.putInt(Constants.APP_PREFERENCES_KEY_5_X, (int) ViewHelper.getX(key5));
        editor.putInt(Constants.APP_PREFERENCES_KEY_5_Y, (int) ViewHelper.getY(key5));
        editor.putInt(Constants.APP_PREFERENCES_KEY_6_X, (int) ViewHelper.getX(key6));
        editor.putInt(Constants.APP_PREFERENCES_KEY_6_Y, (int) ViewHelper.getY(key6));
        editor.putInt(Constants.APP_PREFERENCES_KEY_7_X, (int) ViewHelper.getX(key7));
        editor.putInt(Constants.APP_PREFERENCES_KEY_7_Y, (int) ViewHelper.getY(key7));
        editor.putInt(Constants.APP_PREFERENCES_KEY_8_X, (int) ViewHelper.getX(key8));
        editor.putInt(Constants.APP_PREFERENCES_KEY_8_Y, (int) ViewHelper.getY(key8));
        editor.putInt(Constants.APP_PREFERENCES_KEY_9_X, (int) ViewHelper.getX(key9));
        editor.putInt(Constants.APP_PREFERENCES_KEY_9_Y, (int) ViewHelper.getY(key9));

        editor.commit();
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
		editor.commit();
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
		editor.commit();
		return size;
	}

	public void setAlphaToSharedPreferences(String name, float opacity) {

		Editor editor = Settings.edit();
		editor.putFloat(name, opacity);
		editor.commit();
	}

	public void setSizeToSharedPreferences(String name, int size) {

		Editor editor = Settings.edit();
		editor.putInt(name, size);
		editor.commit();
	}

	public void setButtonsOpacity() {
		if (buttonFlag == 1)
            AlphaView.setAlphaForView(buttonRun, setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_RUN_OPACITY));
		else if (buttonFlag == 2)
            AlphaView.setAlphaForView(buttonConsole, setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CONSOLE_OPACITY));
        else if (buttonFlag == 3)
            AlphaView.setAlphaForView(buttonChangePerson, setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_OPACITY));
        else if (buttonFlag == 4)
            AlphaView.setAlphaForView(buttonWait,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WAIT_OPACITY));
		else if (buttonFlag == 5)
            AlphaView.setAlphaForView(buttonTouch,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_TOUCH_OPACITY));
        else if (buttonFlag == 6)
            AlphaView.setAlphaForView(buttonDiary,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_DIARY_OPACITY));
        else if (buttonFlag == 7)
            AlphaView.setAlphaForView(buttonPause, setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_PAUSE_OPACITY));
        if (buttonFlag == 8)
            AlphaView.setAlphaForView(buttonLoad, setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_LOAD_OPACITY));
        else if (buttonFlag == 9)
            AlphaView.setAlphaForView(buttonSave,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_SAVE_OPACITY));
		else if (buttonFlag == 10)
            AlphaView.setAlphaForView(buttonWeapon,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WEAPON_OPACITY));
        else if (buttonFlag == 11)
            AlphaView.setAlphaForView(buttonInventory,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_INVENTORY_OPACITY));
		else if (buttonFlag == 12)
            AlphaView.setAlphaForView(buttonJump,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_JUMP_OPACITY));
		else if (buttonFlag == 13)
            AlphaView.setAlphaForView(buttonFire,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_FIRE_OPACITY));
        else if (buttonFlag == 14)
            AlphaView.setAlphaForView(buttonMagic,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_MAGIC_OPACITY));
        else if (buttonFlag == 15)
            AlphaView.setAlphaForView(buttonUse,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_USE_OPACITY));
        else if (buttonFlag == 16)
            AlphaView.setAlphaForView(buttonCrouch,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CROUCH_OPACITY));
        else if (buttonFlag == 17)
            AlphaView.setAlphaForView(joystick,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_JOYSTICK_OPACITY));
        else if (buttonFlag == 18)
            AlphaView.setAlphaForView(f1,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_F1_OPACITY));
        else if (buttonFlag == 19)
            AlphaView.setAlphaForView(showPanel,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_OPASITY));
		else if (buttonFlag == 20)
            AlphaView.setAlphaForView(key0,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_0_OPACITY));
        else if (buttonFlag == 21)
            AlphaView.setAlphaForView(key1,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_1_OPACITY));
		else if (buttonFlag == 22)
            AlphaView.setAlphaForView(key2,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_2_OPACITY));
        else if (buttonFlag == 23)
            AlphaView.setAlphaForView(key3,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_3_OPACITY));
        else if (buttonFlag == 24)
            AlphaView.setAlphaForView(key4,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_4_OPACITY));
        else if (buttonFlag == 25)
            AlphaView.setAlphaForView(key5,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_5_OPACITY));
        else if (buttonFlag == 26)
            AlphaView.setAlphaForView(key6,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_6_OPACITY));
        else if (buttonFlag == 27)
            AlphaView.setAlphaForView(key7,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_7_OPACITY));
        else if (buttonFlag == 28)
            AlphaView.setAlphaForView(key8,setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_8_OPACITY));
        else if (buttonFlag == 29)
            AlphaView.setAlphaForView(key9, setAlphaFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_9_OPACITY));

	}

	public void setButtonsSize() {
		if (buttonFlag == 1)
			buttonRun
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonRun,
							(int) ViewHelper.getX(buttonRun),
							(int) ViewHelper.getY(buttonRun),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_RUN_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_RUN_SIZE)));
		else if (buttonFlag == 2)
			buttonConsole
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonConsole,
									(int) ViewHelper.getX(buttonConsole),
									(int) ViewHelper.getY(buttonConsole),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CONSOLE_SIZE),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CONSOLE_SIZE)));

		else if (buttonFlag == 3)
			buttonChangePerson
					.setLayoutParams(ControlsParams
							.coordinatesConfigureControls(
									buttonChangePerson,
									(int) ViewHelper.getX(buttonChangePerson),
									(int) ViewHelper.getY(buttonChangePerson),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_SIZE),
									setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CHANGEPERSON_SIZE)));
		else if (buttonFlag == 4)
			buttonWait
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonWait,
							(int) ViewHelper.getX(buttonWait),
							(int) ViewHelper.getY(buttonWait),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WAIT_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WAIT_SIZE)));
		else if (buttonFlag == 5)
			buttonTouch
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonTouch,
							(int) ViewHelper.getX(buttonTouch),
							(int) ViewHelper.getY(buttonTouch),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_TOUCH_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_TOUCH_SIZE)));
		else if (buttonFlag == 6)
			buttonDiary
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonDiary,
							(int) ViewHelper.getX(buttonDiary),
							(int) ViewHelper.getY(buttonDiary),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_DIARY_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_DIARY_SIZE)));
		else if (buttonFlag == 7)
			buttonPause
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonPause,
							(int) ViewHelper.getX(buttonPause),
							(int) ViewHelper.getY(buttonPause),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_PAUSE_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_PAUSE_SIZE)));

		if (buttonFlag == 8)
			buttonLoad
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                            buttonLoad,
                            (int) ViewHelper.getX(buttonLoad),
                            (int) ViewHelper.getY(buttonLoad),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_LOAD_SIZE),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_LOAD_SIZE)));

		else if (buttonFlag == 9)
			buttonSave
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
							buttonSave,
							(int) ViewHelper.getX(buttonSave),
							(int) ViewHelper.getY(buttonSave),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_SAVE_SIZE),
							setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_SAVE_SIZE)));

		else if (buttonFlag == 10)
			buttonWeapon
					.setLayoutParams(ControlsParams
                            .coordinatesConfigureControls(
                                    buttonWeapon,
                                    (int) ViewHelper.getX(buttonWeapon),
                                    (int) ViewHelper.getY(buttonWeapon),
                                    setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WEAPON_SIZE),
                                    setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_WEAPON_SIZE)));

		else if (buttonFlag == 11)
			buttonInventory
					.setLayoutParams(ControlsParams
                            .coordinatesConfigureControls(
                                    buttonInventory,
                                    (int) ViewHelper.getX(buttonInventory),
                                    (int) ViewHelper.getY(buttonInventory),
                                    setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_INVENTORY_SIZE),
                                    setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_INVENTORY_SIZE)));

		else if (buttonFlag == 12)
			buttonJump
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                            buttonJump,
                            (int) ViewHelper.getX(buttonJump),
                            (int) ViewHelper.getY(buttonJump),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_JUMP_SIZE),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_JUMP_SIZE)));

		else if (buttonFlag == 13)
			buttonFire
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                            buttonFire,
                            (int) ViewHelper.getX(buttonFire),
                            (int) ViewHelper.getY(buttonFire),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_FIRE_SIZE),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_FIRE_SIZE)));

		else if (buttonFlag == 14)
			buttonMagic
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                            buttonMagic,
                            (int) ViewHelper.getX(buttonMagic),
                            (int) ViewHelper.getY(buttonMagic),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_MAGIC_SIZE),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_MAGIC_SIZE)));
		else if (buttonFlag == 15)
			buttonUse
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                            buttonUse,
                            (int) ViewHelper.getX(buttonUse),
                            (int) ViewHelper.getY(buttonUse),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_USE_SIZE),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_USE_SIZE)));

		else if (buttonFlag == 16)
			buttonCrouch
					.setLayoutParams(ControlsParams
                            .coordinatesConfigureControls(
                                    buttonCrouch,
                                    (int) ViewHelper.getX(buttonCrouch),
                                    (int) ViewHelper.getY(buttonCrouch),
                                    setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CROUCH_SIZE),
                                    setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_CROUCH_SIZE)));
		else if (buttonFlag == 17)
			joystick.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					joystick,
					(int) ViewHelper.getX(joystick),
					(int) ViewHelper.getY(joystick),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_JOYSTICK_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_JOYSTICK_SIZE)));
		else if (buttonFlag == 18)
			f1.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					f1,
					(int) ViewHelper.getX(f1),
					(int) ViewHelper.getY(f1),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_F1_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_F1_SIZE)));
		else if (buttonFlag == 19)
			showPanel
					.setLayoutParams(ControlsParams.coordinatesConfigureControls(
                            showPanel,
                            (int) ViewHelper.getX(showPanel),
                            (int) ViewHelper.getY(showPanel),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_SIZE),
                            setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_BUTTON_SHOWPANEL_SIZE)));
		else if (buttonFlag == 20)
			key0.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key0,
					(int) ViewHelper.getX(key0),
					(int)ViewHelper.getY(key0),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_0_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_0_SIZE)));
		else if (buttonFlag == 21)
			key1.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key1,
					(int) ViewHelper.getX(key1),
					(int) ViewHelper.getY(key1),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_1_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_1_SIZE)));
		else if (buttonFlag == 22)
			key2.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key2,
					(int)ViewHelper.getX(key2),
					(int) key2.getY(),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_2_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_2_SIZE)));
		else if (buttonFlag == 23)
			key3.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key3,
					(int) ViewHelper.getX(key3),
					(int) ViewHelper.getY(key3),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_3_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_3_SIZE)));
		else if (buttonFlag == 24)
			key4.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key4,
					(int) ViewHelper.getX(key4),
					(int) ViewHelper.getY(key4),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_4_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_4_SIZE)));
		else if (buttonFlag == 25)
			key5.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key5,
					(int) ViewHelper.getX(key5),
					(int) ViewHelper.getY(key5),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_5_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_5_SIZE)));
		else if (buttonFlag == 26)
			key6.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key6,
					(int) ViewHelper.getX(key6),
					(int) ViewHelper.getY(key6),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_6_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_6_SIZE)));
		else if (buttonFlag == 27)
			key7.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key7,
					(int)ViewHelper.getX(key7),
					(int) ViewHelper.getY(key7),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_7_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_7_SIZE)));

		else if (buttonFlag == 28)
			key8.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key8,
					(int)ViewHelper.getX(key8),
					(int) ViewHelper.getY(key8),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_8_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_8_SIZE)));
		else if (buttonFlag == 29)
			key9.setLayoutParams(ControlsParams.coordinatesConfigureControls(
					key9,
					(int)ViewHelper.getX(key9),
					(int) ViewHelper.getY(key9),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_9_SIZE),
					setSizeFromButtonToSharedPreferences(Constants.APP_PREFERENCES_KEY_9_SIZE)));

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		ScreenScaler.textScaler(buttonOpacity, 4);
		ScreenScaler.textScaler(buttonOpacity1, 4);
		ScreenScaler.textScaler(buttonSize, 4);
		ScreenScaler.textScaler(buttonSize1, 4);
		ScreenScaler.textScaler(showPanel,4 );
		ScreenScaler.textScaler(f1,4 );
		ScreenScaler.textScaler(button,2.7f );


	}

}

package org.libsdl.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.lang.reflect.Method;

import com.libopenmw.openmw.Constants;
import com.libopenmw.openmw.Controls;
import com.libopenmw.openmw.ControlsParams;
import com.libopenmw.openmw.CoordinatesAllScreens;
import com.libopenmw.openmw.JoystickView;
import com.libopenmw.openmw.MainActivity;
import com.libopenmw.openmw.R;
import com.libopenmw.openmw.TouchCamera;

import android.app.*;
import android.content.*;
import android.view.*;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.os.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.graphics.*;
import android.media.*;
import android.hardware.*;

/**
 * SDL Activity
 */
public class SDLActivity extends Activity {
	private static final String TAG = "SDL";

	// Keep track of the paused state
	public static boolean mIsPaused, mIsSurfaceReady, mHasFocus;
	public static boolean mExitCalledFromJava;
	public boolean enableTouch = false;
	public boolean crouchFlag = false;
	public boolean esc = false;

	// Main components
	protected static SDLActivity mSingleton;
	protected static SDLSurface mSurface;
	public static Surface Surface;
	protected static View mTextEdit;
	protected static ViewGroup mLayout;
	protected static SDLJoystickHandler mJoystickHandler;

	// This is what SDL runs in. It invokes SDL_main(), eventually
	protected static Thread mSDLThread;
	public boolean hideControls = false;

	// Audio
	protected static AudioTrack mAudioTrack;

	Context context;

	// Load the .so
	static {
		System.loadLibrary("SDL2");
			System.loadLibrary("openal");

	
		// System.loadLibrary("avfilter-3");
		// System.loadLibrary("avdevice-54");

		// System.loadLibrary("bullet");
		// System.loadLibrary("SDL2");
		System.loadLibrary("openmw");

		// System.loadLibrary("SDL2_image");
		// System.loadLibrary("SDL2_mixer");
		// System.loadLibrary("SDL2_net");
		// System.loadLibrary("SDL2_ttf");
		// System.loadLibrary("main");
	}

	public static void initialize() {
		// The static nature of the singleton and Android quirkyness force us to
		// initialize everything here
		// Otherwise, when exiting the app and returning to it, these variables
		// *keep* their pre exit values
		mSingleton = null;
		mSurface = null;
		Surface = null;
		mTextEdit = null;
		mLayout = null;
		mJoystickHandler = null;
		mSDLThread = null;
		mAudioTrack = null;
		mExitCalledFromJava = false;
		mIsPaused = false;
		mIsSurfaceReady = false;
		mHasFocus = true;
	}

	// Setup
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.v("SDL", "onCreate():" + mSingleton);
		super.onCreate(savedInstanceState);

		SDLActivity.initialize();
		// So we can call stuff from static callbacks
		mSingleton = this;

		File inputfile = new File(
				"/sdcard/libopenmw/data/Video/bethesda logo.bik");
		if (inputfile.exists())
			inputfile.delete();

		// Set up the surface
		mSurface = new SDLSurface(getApplication());

		if (Build.VERSION.SDK_INT >= 12) {
			mJoystickHandler = new SDLJoystickHandler_API12();
		} else {
			mJoystickHandler = new SDLJoystickHandler();
		}

		mLayout = new AbsoluteLayout(this);

		context = this;

		mLayout.addView(mSurface);

		// Perform action on click

		// Surface.
		setContentView(mLayout);

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

			buttonRun.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_CAPS_LOCK);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_CAPS_LOCK);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonConsole = (ImageButton) findViewById(R.id.buttonconsole);

			buttonConsole.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_F2);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_F2);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonChangePerson = (ImageButton) findViewById(R.id.buttonchangeperson);

			buttonChangePerson.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_TAB);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_TAB);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonWait = (ImageButton) findViewById(R.id.buttonwait);

			buttonWait.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_T);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_T);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final Button buttonTouch = (Button) findViewById(R.id.buttontouch);
			final TouchCamera touch = (TouchCamera) findViewById(R.id.superTouch);

			buttonTouch.setText("off");

			buttonTouch.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED

						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						if (enableTouch == false) {
							buttonTouch.setText("on");
							enableTouch = true;
							touch.setVisibility(TouchCamera.VISIBLE);

						} else {
							enableTouch = false;
							touch.setVisibility(TouchCamera.INVISIBLE);
							buttonTouch.setText("off");
						}
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonLoad = (ImageButton) findViewById(R.id.buttonsuperload);

			buttonLoad.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_F9);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_F9);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonSave = (ImageButton) findViewById(R.id.buttonsupersave);

			buttonSave.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_F5);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_F5);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonWeapon = (ImageButton) findViewById(R.id.buttonweapon);

			buttonWeapon.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_F);
						return true; // if you want to handle the touch
						// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_F);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonInventory = (ImageButton) findViewById(R.id.buttoninventory);

			buttonInventory.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED

						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_L);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_L);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonJump = (ImageButton) findViewById(R.id.buttonsuperjump);

			buttonJump.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_E);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_E);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonFire = (ImageButton) findViewById(R.id.buttonFire);

			buttonFire.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_Z);

						// (MotionEvent.BUTTON_PRIMARY);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_Z);

						// SDLActivity.onNativeKeyUp(MotionEvent.BUTTON_PRIMARY);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonMagic = (ImageButton) findViewById(R.id.buttonMagic);

			buttonMagic.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_R);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_R);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});
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

			buttonDiary.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_J);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_J);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

			final ImageButton buttonUse = (ImageButton) findViewById(R.id.buttonUse);

			buttonUse.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:

						// PRESSED
						SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_SPACE);
						return true; // if you want to handle the touch
										// event
					case MotionEvent.ACTION_UP:
						// RELEASED
						SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_SPACE);
						return true; // if you want to handle the touch
										// event
					}
					return false;
				}
			});

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
							
							buttonChangePerson.setVisibility(ImageButton.VISIBLE);
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

	// Events
	@Override
	protected void onPause() {
		Log.v("SDL", "onPause()");
		super.onPause();
		SDLActivity.handlePause();
	}

	@Override
	protected void onResume() {
		Log.v("SDL", "onResume()");
		super.onResume();
		SDLActivity.handleResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		Log.v("SDL", "onWindowFocusChanged(): " + hasFocus);

		SDLActivity.mHasFocus = hasFocus;
		if (hasFocus) {
			SDLActivity.handleResume();
		}
	}

	@Override
	public void onLowMemory() {
		Log.v("SDL", "onLowMemory()");
		super.onLowMemory();
		SDLActivity.nativeLowMemory();
	}

	@Override
	protected void onDestroy() {
		Log.v("SDL", "onDestroy()");
		// Send a quit message to the application
		SDLActivity.mExitCalledFromJava = true;
		SDLActivity.nativeQuit();

		// Now wait for the SDL thread to quit
		if (SDLActivity.mSDLThread != null) {
			try {
				SDLActivity.mSDLThread.join();
			} catch (Exception e) {
				Log.v("SDL", "Problem stopping thread: " + e);
			}
			SDLActivity.mSDLThread = null;

			// Log.v("SDL", "Finished waiting for SDL thread");
		}

		super.onDestroy();
		// Reset everything in case the user re opens the app
		SDLActivity.initialize();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int keyCode = event.getKeyCode();
		// Ignore certain special keys so they're handled by Android
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
				|| keyCode == KeyEvent.KEYCODE_VOLUME_UP
				|| keyCode == KeyEvent.KEYCODE_CAMERA || keyCode == 168 || /*
																			 * API
																			 * 11
																			 * :
																			 * KeyEvent
																			 * .
																			 * KEYCODE_ZOOM_IN
																			 */
				keyCode == 169 /* API 11: KeyEvent.KEYCODE_ZOOM_OUT */
		) {
			return false;
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * Called by onPause or surfaceDestroyed. Even if surfaceDestroyed is the
	 * first to be called, mIsSurfaceReady should still be set to 'true' during
	 * the call to onPause (in a usual scenario).
	 */
	public static void handlePause() {
		if (!SDLActivity.mIsPaused && SDLActivity.mIsSurfaceReady) {
			SDLActivity.mIsPaused = true;
			SDLActivity.nativePause();
			mSurface.enableSensor(Sensor.TYPE_ACCELEROMETER, false);
		}
	}

	/**
	 * Called by onResume or surfaceCreated. An actual resume should be done
	 * only when the surface is ready. Note: Some Android variants may send
	 * multiple surfaceChanged events, so we don't need to resume every time we
	 * get one of those events, only if it comes after surfaceDestroyed
	 */
	public static void handleResume() {
		if (SDLActivity.mIsPaused && SDLActivity.mIsSurfaceReady
				&& SDLActivity.mHasFocus) {
			SDLActivity.mIsPaused = false;
			SDLActivity.nativeResume();
			mSurface.handleResume();
		}
	}

	
	
	/* The native thread has finished */
	public static void handleNativeExit() {
		SDLActivity.mSDLThread = null;
		mSingleton.finish();
	}

	// Messages from the SDLMain thread
	static final int COMMAND_CHANGE_TITLE = 1;
	static final int COMMAND_UNUSED = 2;
	static final int COMMAND_TEXTEDIT_HIDE = 3;

	protected static final int COMMAND_USER = 0x8000;

	/**
	 * This method is called by SDL if SDL did not handle a message itself. This
	 * happens if a received message contains an unsupported command. Method can
	 * be overwritten to handle Messages in a different class.
	 * 
	 * @param command
	 *            the command of the message.
	 * @param param
	 *            the parameter of the message. May be null.
	 * @return if the message was handled in overridden method.
	 */
	protected boolean onUnhandledMessage(int command, Object param) {
		return false;
	}

	/**
	 * A Handler class for Messages from native SDL applications. It uses
	 * current Activities as target (e.g. for the title). static to prevent
	 * implicit references to enclosing object.
	 */
	protected static class SDLCommandHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Context context = getContext();
			if (context == null) {
				Log.e(TAG, "error handling message, getContext() returned null");
				return;
			}
			switch (msg.arg1) {
			case COMMAND_CHANGE_TITLE:
				if (context instanceof Activity) {
					((Activity) context).setTitle((String) msg.obj);
				} else {
					Log.e(TAG,
							"error handling message, getContext() returned no Activity");
				}
				break;
			case COMMAND_TEXTEDIT_HIDE:
				if (mTextEdit != null) {
					mTextEdit.setVisibility(View.GONE);

					InputMethodManager imm = (InputMethodManager) context
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mTextEdit.getWindowToken(), 0);
				}
				break;

			default:
				if ((context instanceof SDLActivity)
						&& !((SDLActivity) context).onUnhandledMessage(
								msg.arg1, msg.obj)) {
					Log.e(TAG, "error handling message, command is " + msg.arg1);
				}
			}
		}
	}

	// Handler for the messages
	Handler commandHandler = new SDLCommandHandler();

	// Send a message from the SDLMain thread
	boolean sendCommand(int command, Object data) {
		Message msg = commandHandler.obtainMessage();
		msg.arg1 = command;
		msg.obj = data;
		return commandHandler.sendMessage(msg);
	}

	// C functions we call
	public static native int nativeInit();

	public static native void nativeLowMemory();

	public static native void nativeQuit();

	public static native void nativePause();

	public static native void nativeResume();

	public static native void onNativeResize(int x, int y, int format);

	public static native int onNativePadDown(int device_id, int keycode);

	public static native int onNativePadUp(int device_id, int keycode);

	public static native void onNativeJoy(int device_id, int axis, float value);

	public static native void onNativeHat(int device_id, int hat_id, int x,
			int y);

	public static native void onNativeKeyDown(int keycode);

	public static native void onNativeKeyUp(int keycode);

	public static native void onNativeKeyboardFocusLost();

	public static native void onNativeTouch(int touchDevId,
			int pointerFingerId, int action, float x, float y, float p);

	public static native void onNativeAccel(float x, float y, float z);

	public static native void onNativeSurfaceChanged();

	public static native void onNativeSurfaceDestroyed();

	public static native void nativeFlipBuffers();

	public static native int nativeAddJoystick(int device_id, String name,
			int is_accelerometer, int nbuttons, int naxes, int nhats, int nballs);

	public static native int nativeRemoveJoystick(int device_id);

	public static native String nativeGetHint(String name);

	/**
	 * This method is called by SDL using JNI.
	 */
	public static void flipBuffers() {
		SDLActivity.nativeFlipBuffers();
	}

	/**
	 * This method is called by SDL using JNI.
	 */
	public static boolean setActivityTitle(String title) {
		// Called from SDLMain() thread and can't directly affect the view
		return mSingleton.sendCommand(COMMAND_CHANGE_TITLE, title);
	}

	/**
	 * This method is called by SDL using JNI.
	 */
	public static boolean sendMessage(int command, int param) {
		return mSingleton.sendCommand(command, Integer.valueOf(param));
	}

	/**
	 * This method is called by SDL using JNI.
	 */
	public static Context getContext() {
		return mSingleton;
	}

	/**
	 * This method is called by SDL using JNI.
	 * 
	 * @return result of getSystemService(name) but executed on UI thread.
	 */
	public Object getSystemServiceFromUiThread(final String name) {
		final Object lock = new Object();
		final Object[] results = new Object[2]; // array for writable variables
		synchronized (lock) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					synchronized (lock) {
						results[0] = getSystemService(name);
						results[1] = Boolean.TRUE;
						lock.notify();
					}
				}
			});
			if (results[1] == null) {
				try {
					lock.wait();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
		return results[0];
	}

	static class ShowTextInputTask implements Runnable {
		/*
		 * This is used to regulate the pan&scan method to have some offset from
		 * the bottom edge of the input region and the top edge of an input
		 * method (soft keyboard)
		 */
		static final int HEIGHT_PADDING = 15;

		public int x, y, w, h;

		public ShowTextInputTask(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		@Override
		public void run() {
			AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(
					w, h + HEIGHT_PADDING, x, y);

			if (mTextEdit == null) {
				mTextEdit = new DummyEdit(getContext());

				mLayout.addView(mTextEdit, params);
			} else {
				mTextEdit.setLayoutParams(params);
			}

			mTextEdit.setVisibility(View.VISIBLE);
			mTextEdit.requestFocus();

			InputMethodManager imm = (InputMethodManager) getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(mTextEdit, 0);
		}
	}

	/**
	 * This method is called by SDL using JNI.
	 */
	public static boolean showTextInput(int x, int y, int w, int h) {
		// Transfer the task to the main thread as a Runnable
		return mSingleton.commandHandler
				.post(new ShowTextInputTask(x, y, w, h));
	}

	/**
	 * This method is called by SDL using JNI.
	 */
	public static Surface getNativeSurface() {
		return SDLActivity.mSurface.getNativeSurface();
	}

	// Audio

	/**
	 * This method is called by SDL using JNI.
	 */
	public static int audioInit(int sampleRate, boolean is16Bit,
			boolean isStereo, int desiredFrames) {
		int channelConfig = isStereo ? AudioFormat.CHANNEL_CONFIGURATION_STEREO
				: AudioFormat.CHANNEL_CONFIGURATION_MONO;
		int audioFormat = is16Bit ? AudioFormat.ENCODING_PCM_16BIT
				: AudioFormat.ENCODING_PCM_8BIT;
		int frameSize = (isStereo ? 2 : 1) * (is16Bit ? 2 : 1);

		Log.v("SDL", "SDL audio: wanted " + (isStereo ? "stereo" : "mono")
				+ " " + (is16Bit ? "16-bit" : "8-bit") + " "
				+ (sampleRate / 1000f) + "kHz, " + desiredFrames
				+ " frames buffer");

		// Let the user pick a larger buffer if they really want -- but ye
		// gods they probably shouldn't, the minimums are horrifyingly high
		// latency already
		desiredFrames = Math.max(
				desiredFrames,
				(AudioTrack.getMinBufferSize(sampleRate, channelConfig,
						audioFormat) + frameSize - 1)
						/ frameSize);

		if (mAudioTrack == null) {
			mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate,
					channelConfig, audioFormat, desiredFrames * frameSize,
					AudioTrack.MODE_STREAM);

			// Instantiating AudioTrack can "succeed" without an exception and
			// the track may still be invalid
			// Ref:
			// https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/media/java/android/media/AudioTrack.java
			// Ref:
			// http://developer.android.com/reference/android/media/AudioTrack.html#getState()

			if (mAudioTrack.getState() != AudioTrack.STATE_INITIALIZED) {
				Log.e("SDL", "Failed during initialization of Audio Track");
				mAudioTrack = null;
				return -1;
			}

			mAudioTrack.play();
		}

		Log.v("SDL",
				"SDL audio: got "
						+ ((mAudioTrack.getChannelCount() >= 2) ? "stereo"
								: "mono")
						+ " "
						+ ((mAudioTrack.getAudioFormat() == AudioFormat.ENCODING_PCM_16BIT) ? "16-bit"
								: "8-bit") + " "
						+ (mAudioTrack.getSampleRate() / 1000f) + "kHz, "
						+ desiredFrames + " frames buffer");

		return 0;
	}

	/**
	 * This method is called by SDL using JNI.
	 */
	public static void audioWriteShortBuffer(short[] buffer) {
		for (int i = 0; i < buffer.length;) {
			int result = mAudioTrack.write(buffer, i, buffer.length - i);
			if (result > 0) {
				i += result;
			} else if (result == 0) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// Nom nom
				}
			} else {
				Log.w("SDL", "SDL audio: error return from write(short)");
				return;
			}
		}
	}

	/**
	 * This method is called by SDL using JNI.
	 */
	public static void audioWriteByteBuffer(byte[] buffer) {
		for (int i = 0; i < buffer.length;) {
			int result = mAudioTrack.write(buffer, i, buffer.length - i);
			if (result > 0) {
				i += result;
			} else if (result == 0) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// Nom nom
				}
			} else {
				Log.w("SDL", "SDL audio: error return from write(byte)");
				return;
			}
		}
	}

	/**
	 * This method is called by SDL using JNI.
	 */
	public static void audioQuit() {
		if (mAudioTrack != null) {
			mAudioTrack.stop();
			mAudioTrack = null;
		}
	}

	// Input

	/**
	 * This method is called by SDL using JNI.
	 * 
	 * @return an array which may be empty but is never null.
	 */
	public static int[] inputGetInputDeviceIds(int sources) {
		int[] ids = InputDevice.getDeviceIds();
		int[] filtered = new int[ids.length];
		int used = 0;
		for (int i = 0; i < ids.length; ++i) {
			InputDevice device = InputDevice.getDevice(ids[i]);
			if ((device != null) && ((device.getSources() & sources) != 0)) {
				filtered[used++] = device.getId();
			}
		}
		return Arrays.copyOf(filtered, used);
	}

	// Joystick glue code, just a series of stubs that redirect to the
	// SDLJoystickHandler instance
	public static boolean handleJoystickMotionEvent(MotionEvent event) {
		return mJoystickHandler.handleMotionEvent(event);
	}

	/**
	 * This method is called by SDL using JNI.
	 */
	public static void pollInputDevices() {
		if (SDLActivity.mSDLThread != null) {
			mJoystickHandler.pollInputDevices();
		}
	}

	// APK extension files support

	/** com.android.vending.expansion.zipfile.ZipResourceFile object or null. */
	private Object expansionFile;

	/**
	 * com.android.vending.expansion.zipfile.ZipResourceFile's getInputStream()
	 * or null.
	 */
	private Method expansionFileMethod;

	public InputStream openAPKExtensionInputStream(String fileName)
			throws IOException {
		// Get a ZipResourceFile representing a merger of both the main and
		// patch files
		if (expansionFile == null) {
			Integer mainVersion = Integer
					.parseInt(nativeGetHint("SDL_ANDROID_APK_EXPANSION_MAIN_FILE_VERSION"));
			Integer patchVersion = Integer
					.parseInt(nativeGetHint("SDL_ANDROID_APK_EXPANSION_PATCH_FILE_VERSION"));

			try {
				// To avoid direct dependency on Google APK extension library
				// that is
				// not a part of Android SDK we access it using reflection
				expansionFile = Class
						.forName(
								"com.android.vending.expansion.zipfile.APKExpansionSupport")
						.getMethod("getAPKExpansionZipFile", Context.class,
								int.class, int.class)
						.invoke(null, this, mainVersion, patchVersion);

				expansionFileMethod = expansionFile.getClass().getMethod(
						"getInputStream", String.class);
			} catch (Exception ex) {
				ex.printStackTrace();
				expansionFile = null;
				expansionFileMethod = null;
			}
		}

		// Get an input stream for a known file inside the expansion file ZIPs
		InputStream fileStream;
		try {
			fileStream = (InputStream) expansionFileMethod.invoke(
					expansionFile, fileName);
		} catch (Exception ex) {
			ex.printStackTrace();
			fileStream = null;
		}

		if (fileStream == null) {
			throw new IOException();
		}

		return fileStream;
	}
}

/**
 * Simple nativeInit() runnable
 */
class SDLMain implements Runnable {
	@Override
	public void run() {
		// Runs SDL_main()
		SDLActivity.nativeInit();

		// Log.v("SDL", "SDL thread terminated");
	}
}

/**
 * SDLSurface. This is what we draw on, so we need to know when it's created in
 * order to do anything useful.
 * 
 * Because of this, that's where we set up the SDL thread
 */
class SDLSurface extends SurfaceView implements SurfaceHolder.Callback,
		View.OnKeyListener, View.OnTouchListener, SensorEventListener {

	// Sensors
	protected static SensorManager mSensorManager;
	protected static Display mDisplay;

	// Keep track of the surface size to normalize touch events
	protected static float mWidth, mHeight;

	// Startup
	public SDLSurface(Context context) {
		super(context);
		getHolder().addCallback(this);

		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		setOnKeyListener(this);
		setOnTouchListener(this);

		mDisplay = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

		if (Build.VERSION.SDK_INT >= 12) {
			setOnGenericMotionListener(new SDLGenericMotionListener_API12());
		}

		// Some arbitrary defaults to avoid a potential division by zero
		mWidth = 1.0f;
		mHeight = 1.0f;
	}

	public void handleResume() {
		setFocusable(true);
		setFocusableInTouchMode(true);
		requestFocus();
		setOnKeyListener(this);
		setOnTouchListener(this);
		enableSensor(Sensor.TYPE_ACCELEROMETER, true);
	}

	public Surface getNativeSurface() {
		return getHolder().getSurface();
	}

	// Called when we have a valid drawing surface
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.v("SDL", "surfaceCreated()");
		holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
	}

	// Called when we lose the surface
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.v("SDL", "surfaceDestroyed()");
		// Call this *before* setting mIsSurfaceReady to 'false'
		SDLActivity.handlePause();
		SDLActivity.mIsSurfaceReady = false;
		SDLActivity.onNativeSurfaceDestroyed();
	}

	// Called when the surface is resized
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.v("SDL", "surfaceChanged()");

		int sdlFormat = 0x15151002; // SDL_PIXELFORMAT_RGB565 by default
		switch (format) {
		case PixelFormat.A_8:
			Log.v("SDL", "pixel format A_8");
			break;
		case PixelFormat.LA_88:
			Log.v("SDL", "pixel format LA_88");
			break;
		case PixelFormat.L_8:
			Log.v("SDL", "pixel format L_8");
			break;
		case PixelFormat.RGBA_4444:
			Log.v("SDL", "pixel format RGBA_4444");
			sdlFormat = 0x15421002; // SDL_PIXELFORMAT_RGBA4444
			break;
		case PixelFormat.RGBA_5551:
			Log.v("SDL", "pixel format RGBA_5551");
			sdlFormat = 0x15441002; // SDL_PIXELFORMAT_RGBA5551
			break;
		case PixelFormat.RGBA_8888:
			Log.v("SDL", "pixel format RGBA_8888");
			sdlFormat = 0x16462004; // SDL_PIXELFORMAT_RGBA8888
			break;
		case PixelFormat.RGBX_8888:
			Log.v("SDL", "pixel format RGBX_8888");
			sdlFormat = 0x16261804; // SDL_PIXELFORMAT_RGBX8888
			break;
		case PixelFormat.RGB_332:
			Log.v("SDL", "pixel format RGB_332");
			sdlFormat = 0x14110801; // SDL_PIXELFORMAT_RGB332
			break;
		case PixelFormat.RGB_565:
			Log.v("SDL", "pixel format RGB_565");
			sdlFormat = 0x15151002; // SDL_PIXELFORMAT_RGB565
			break;
		case PixelFormat.RGB_888:
			Log.v("SDL", "pixel format RGB_888");
			// Not sure this is right, maybe SDL_PIXELFORMAT_RGB24 instead?
			sdlFormat = 0x16161804; // SDL_PIXELFORMAT_RGB888
			break;
		default:
			Log.v("SDL", "pixel format unknown " + format);
			break;
		}

		mWidth = width;
		mHeight = height;

		SDLActivity.onNativeResize(width, height, sdlFormat);
		Log.v("SDL", "Window size:" + width + "x" + height);

		// Set mIsSurfaceReady to 'true' *before* making a call to handleResume
		SDLActivity.mIsSurfaceReady = true;
		SDLActivity.onNativeSurfaceChanged();

		if (SDLActivity.mSDLThread == null) {
			// This is the entry point to the C app.
			// Start up the C app thread and enable sensor input for the first
			// time

			SDLActivity.mSDLThread = new Thread(new SDLMain(), "SDLThread");
			enableSensor(Sensor.TYPE_ACCELEROMETER, true);
			SDLActivity.mSDLThread.start();

			// Set up a listener thread to catch when the native thread ends
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						SDLActivity.mSDLThread.join();
					} catch (Exception e) {
					} finally {
						// Native thread has finished
						if (!SDLActivity.mExitCalledFromJava) {
							SDLActivity.handleNativeExit();
						}
					}
				}
			}).start();
		}
	}

	// unused
	@Override
	public void onDraw(Canvas canvas) {
	}

	// Key events
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		// Dispatch the different events depending on where they come from
		// Some SOURCE_DPAD or SOURCE_GAMEPAD are also SOURCE_KEYBOARD
		// So, we try to process them as DPAD or GAMEPAD events first, if that
		// fails we try them as KEYBOARD

		if ((event.getSource() & 0x00000401) != 0 || /* API 12: SOURCE_GAMEPAD */
		(event.getSource() & InputDevice.SOURCE_DPAD) != 0) {

			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (keyCode == 4) {
					SDLActivity.onNativeKeyDown(KeyEvent.KEYCODE_ESCAPE);

					SDLActivity.onNativeKeyUp(KeyEvent.KEYCODE_ESCAPE);

				}
				if (SDLActivity.onNativePadDown(event.getDeviceId(), keyCode) == 0) {
					return true;
				}
			} else if (event.getAction() == KeyEvent.ACTION_UP) {
				if (SDLActivity.onNativePadUp(event.getDeviceId(), keyCode) == 0) {
					return true;
				}
			}
		}

		if ((event.getSource() & InputDevice.SOURCE_KEYBOARD) != 0) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				// Log.v("SDL", "key down: " + keyCode);
				SDLActivity.onNativeKeyDown(keyCode);
				return true;
			} else if (event.getAction() == KeyEvent.ACTION_UP) {
				// Log.v("SDL", "key up: " + keyCode);
				SDLActivity.onNativeKeyUp(keyCode);
				return true;
			}
		}

		return false;
	}

	// Touch events
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		/* Ref: http://developer.android.com/training/gestures/multi.html */
		final int touchDevId = event.getDeviceId();
		final int pointerCount = event.getPointerCount();
		int action = event.getActionMasked();

		int pointerFingerId;
		int i = -1;
		float x, y, p;

		switch (action) {

		case MotionEvent.ACTION_MOVE:
			for (i = 0; i < pointerCount; i++) {
				pointerFingerId = event.getPointerId(i);
				x = event.getX(i) / mWidth;
				y = event.getY(i) / mHeight;
				p = event.getPressure(i);
				SDLActivity.onNativeTouch(touchDevId, pointerFingerId, action,
						x, y, p);
				Log.d("TAG", "X:" + x + "|Y:" + y);
				// Log.d("TAG", "X:" + pointerFingerId+"YX:" + p);
			}
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_DOWN:
			// Primary pointer up/down, the index is always zero
			i = 0;
		case MotionEvent.ACTION_POINTER_UP:

		case MotionEvent.ACTION_POINTER_DOWN:
			// Non primary pointer up/down
			if (i == -1) {
				i = event.getActionIndex();
			}

			pointerFingerId = event.getPointerId(i);
			x = event.getX(i) / mWidth;
			y = event.getY(i) / mHeight;
			p = event.getPressure(i);
			SDLActivity.onNativeTouch(touchDevId, pointerFingerId, action, x,
					y, p);
			break;

		case MotionEvent.ACTION_CANCEL:
			for (i = 0; i < pointerCount; i++) {
				pointerFingerId = event.getPointerId(i);
				x = event.getX(i) / mWidth;
				y = event.getY(i) / mHeight;
				p = event.getPressure(i);
				SDLActivity.onNativeTouch(touchDevId, pointerFingerId,
						MotionEvent.ACTION_UP, x, y, p);
			}
			break;

		default:
			break;
		}

		return true;
	}

	// Sensor events
	public void enableSensor(int sensortype, boolean enabled) {
		// TODO: This uses getDefaultSensor - what if we have >1 accels?
		if (enabled) {
			mSensorManager.registerListener(this,
					mSensorManager.getDefaultSensor(sensortype),
					SensorManager.SENSOR_DELAY_GAME, null);
		} else {
			mSensorManager.unregisterListener(this,
					mSensorManager.getDefaultSensor(sensortype));
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float x, y;
			switch (mDisplay.getRotation()) {
			case Surface.ROTATION_90:
				x = -event.values[1];
				y = event.values[0];
				break;
			case Surface.ROTATION_270:
				x = event.values[1];
				y = -event.values[0];
				break;
			case Surface.ROTATION_180:
				x = -event.values[1];
				y = -event.values[0];
				break;
			default:
				x = event.values[0];
				y = event.values[1];
				break;
			}
			SDLActivity.onNativeAccel(-x / SensorManager.GRAVITY_EARTH, y
					/ SensorManager.GRAVITY_EARTH, event.values[2]
					/ SensorManager.GRAVITY_EARTH - 1);
		}
	}
}

/*
 * This is a fake invisible editor view that receives the input and defines the
 * pan&scan region
 */
class DummyEdit extends View implements View.OnKeyListener {
	InputConnection ic;

	public DummyEdit(Context context) {
		super(context);
		setFocusableInTouchMode(true);
		setFocusable(true);
		setOnKeyListener(this);
	}

	@Override
	public boolean onCheckIsTextEditor() {
		return true;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {

		// This handles the hardware keyboard input
		if (event.isPrintingKey()) {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				ic.commitText(String.valueOf((char) event.getUnicodeChar()), 1);
			}
			return true;
		}

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			SDLActivity.onNativeKeyDown(keyCode);
			return true;
		} else if (event.getAction() == KeyEvent.ACTION_UP) {
			SDLActivity.onNativeKeyUp(keyCode);
			return true;
		}

		return false;
	}

	//
	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		// As seen on StackOverflow:
		// http://stackoverflow.com/questions/7634346/keyboard-hide-event
		// FIXME: Discussion at http://bugzilla.libsdl.org/show_bug.cgi?id=1639
		// FIXME: This is not a 100% effective solution to the problem of
		// detecting if the keyboard is showing or not
		// FIXME: A more effective solution would be to change our Layout from
		// AbsoluteLayout to Relative or Linear
		// FIXME: And determine the keyboard presence doing this:
		// http://stackoverflow.com/questions/2150078/how-to-check-visibility-of-software-keyboard-in-android
		// FIXME: An even more effective way would be if Android provided this
		// out of the box, but where would the fun be in that :)
		if (event.getAction() == KeyEvent.ACTION_UP
				&& keyCode == KeyEvent.KEYCODE_BACK) {
			if (SDLActivity.mTextEdit != null
					&& SDLActivity.mTextEdit.getVisibility() == View.VISIBLE) {
				SDLActivity.onNativeKeyboardFocusLost();
			}
		}
		return super.onKeyPreIme(keyCode, event);
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		ic = new SDLInputConnection(this, true);

		outAttrs.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI | 33554432 /*
																			 * API
																			 * 11
																			 * :
																			 * EditorInfo
																			 * .
																			 * IME_FLAG_NO_FULLSCREEN
																			 */;

		return ic;
	}
}

class SDLInputConnection extends BaseInputConnection {

	public SDLInputConnection(View targetView, boolean fullEditor) {
		super(targetView, fullEditor);

	}

	@Override
	public boolean sendKeyEvent(KeyEvent event) {

		/*
		 * This handles the keycodes from soft keyboard (and IME-translated
		 * input from hardkeyboard)
		 */
		int keyCode = event.getKeyCode();
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (event.isPrintingKey()) {
				commitText(String.valueOf((char) event.getUnicodeChar()), 1);
			}
			SDLActivity.onNativeKeyDown(keyCode);
			return true;
		} else if (event.getAction() == KeyEvent.ACTION_UP) {

			SDLActivity.onNativeKeyUp(keyCode);
			return true;
		}
		return super.sendKeyEvent(event);
	}

	@Override
	public boolean commitText(CharSequence text, int newCursorPosition) {

		nativeCommitText(text.toString(), newCursorPosition);

		return super.commitText(text, newCursorPosition);
	}

	@Override
	public boolean setComposingText(CharSequence text, int newCursorPosition) {

		nativeSetComposingText(text.toString(), newCursorPosition);

		return super.setComposingText(text, newCursorPosition);
	}

	public native void nativeCommitText(String text, int newCursorPosition);

	public native void nativeSetComposingText(String text, int newCursorPosition);

	@Override
	public boolean deleteSurroundingText(int beforeLength, int afterLength) {
		// Workaround to capture backspace key. Ref:
		// http://stackoverflow.com/questions/14560344/android-backspace-in-webview-baseinputconnection
		if (beforeLength == 1 && afterLength == 0) {
			// backspace
			return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
					KeyEvent.KEYCODE_DEL))
					&& super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
							KeyEvent.KEYCODE_DEL));
		}

		return super.deleteSurroundingText(beforeLength, afterLength);
	}
}

/*
 * A null joystick handler for API level < 12 devices (the accelerometer is
 * handled separately)
 */
class SDLJoystickHandler {

	/**
	 * Handles given MotionEvent.
	 * 
	 * @param event
	 *            the event to be handled.
	 * @return if given event was processed.
	 */
	public boolean handleMotionEvent(MotionEvent event) {
		return false;
	}

	/**
	 * Handles adding and removing of input devices.
	 */
	public void pollInputDevices() {
	}
}

/* Actual joystick functionality available for API >= 12 devices */
class SDLJoystickHandler_API12 extends SDLJoystickHandler {

	static class SDLJoystick {
		public int device_id;
		public String name;
		public ArrayList<InputDevice.MotionRange> axes;
		public ArrayList<InputDevice.MotionRange> hats;
	}

	static class RangeComparator implements Comparator<InputDevice.MotionRange> {
		@Override
		public int compare(InputDevice.MotionRange arg0,
				InputDevice.MotionRange arg1) {
			return arg0.getAxis() - arg1.getAxis();
		}
	}

	private ArrayList<SDLJoystick> mJoysticks;

	public SDLJoystickHandler_API12() {

		mJoysticks = new ArrayList<SDLJoystick>();
	}

	@Override
	public void pollInputDevices() {
		int[] deviceIds = InputDevice.getDeviceIds();
		// It helps processing the device ids in reverse order
		// For example, in the case of the XBox 360 wireless dongle,
		// so the first controller seen by SDL matches what the receiver
		// considers to be the first controller

		for (int i = deviceIds.length - 1; i > -1; i--) {
			SDLJoystick joystick = getJoystick(deviceIds[i]);
			if (joystick == null) {
				joystick = new SDLJoystick();
				InputDevice joystickDevice = InputDevice
						.getDevice(deviceIds[i]);
				if ((joystickDevice.getSources() & InputDevice.SOURCE_CLASS_JOYSTICK) != 0) {
					joystick.device_id = deviceIds[i];
					joystick.name = joystickDevice.getName();
					joystick.axes = new ArrayList<InputDevice.MotionRange>();
					joystick.hats = new ArrayList<InputDevice.MotionRange>();

					List<InputDevice.MotionRange> ranges = joystickDevice
							.getMotionRanges();
					Collections.sort(ranges, new RangeComparator());
					for (InputDevice.MotionRange range : ranges) {
						if ((range.getSource() & InputDevice.SOURCE_CLASS_JOYSTICK) != 0) {
							if (range.getAxis() == MotionEvent.AXIS_HAT_X
									|| range.getAxis() == MotionEvent.AXIS_HAT_Y) {
								joystick.hats.add(range);
							} else {
								joystick.axes.add(range);
							}
						}
					}

					mJoysticks.add(joystick);
					SDLActivity.nativeAddJoystick(joystick.device_id,
							joystick.name, 0, -1, joystick.axes.size(),
							joystick.hats.size() / 2, 0);
				}
			}
		}

		/* Check removed devices */
		ArrayList<Integer> removedDevices = new ArrayList<Integer>();
		for (int i = 0; i < mJoysticks.size(); i++) {
			int device_id = mJoysticks.get(i).device_id;
			int j;
			for (j = 0; j < deviceIds.length; j++) {
				if (device_id == deviceIds[j])
					break;
			}
			if (j == deviceIds.length) {
				removedDevices.add(Integer.valueOf(device_id));
			}
		}

		for (int i = 0; i < removedDevices.size(); i++) {
			int device_id = removedDevices.get(i).intValue();
			SDLActivity.nativeRemoveJoystick(device_id);
			for (int j = 0; j < mJoysticks.size(); j++) {
				if (mJoysticks.get(j).device_id == device_id) {
					mJoysticks.remove(j);
					break;
				}
			}
		}
	}

	protected SDLJoystick getJoystick(int device_id) {
		for (int i = 0; i < mJoysticks.size(); i++) {
			if (mJoysticks.get(i).device_id == device_id) {
				return mJoysticks.get(i);
			}
		}
		return null;
	}

	@Override
	public boolean handleMotionEvent(MotionEvent event) {
		if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) != 0) {
			int actionPointerIndex = event.getActionIndex();
			int action = event.getActionMasked();
			switch (action) {
			case MotionEvent.ACTION_MOVE:
				SDLJoystick joystick = getJoystick(event.getDeviceId());
				if (joystick != null) {
					for (int i = 0; i < joystick.axes.size(); i++) {
						InputDevice.MotionRange range = joystick.axes.get(i);
						/* Normalize the value to -1...1 */
						float value = (event.getAxisValue(range.getAxis(),
								actionPointerIndex) - range.getMin())
								/ range.getRange() * 2.0f - 1.0f;
						SDLActivity.onNativeJoy(joystick.device_id, i, value);
					}
					for (int i = 0; i < joystick.hats.size(); i += 2) {
						int hatX = Math.round(event.getAxisValue(joystick.hats
								.get(i).getAxis(), actionPointerIndex));
						int hatY = Math.round(event.getAxisValue(joystick.hats
								.get(i + 1).getAxis(), actionPointerIndex));
						SDLActivity.onNativeHat(joystick.device_id, i / 2,
								hatX, hatY);
					}
				}
				break;
			default:
				break;
			}
		}
		return true;
	}
}

class SDLGenericMotionListener_API12 implements View.OnGenericMotionListener {
	// Generic Motion (mouse hover, joystick...) events go here
	// We only have joysticks yet
	@Override
	public boolean onGenericMotion(View v, MotionEvent event) {
		return SDLActivity.handleJoystickMotionEvent(event);
	}
}

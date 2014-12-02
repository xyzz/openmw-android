package com.libopenmw.openmw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.libsdl.app.SDLActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static Context context;
	public static boolean contols = true;

	public LinearLayout linlaHeaderProgress;
	public SharedPreferences Settings;

	public Thread th;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		context = this;

		linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		Settings = getSharedPreferences(Constants.APP_PREFERENCES,
				Context.MODE_MULTI_PROCESS);

		final CheckBox Box = (CheckBox) findViewById(R.id.checkBox1);

		int hideControlsFlag = Settings.getInt(
				Constants.APP_PREFERENCES_CONTROLS_FLAG, -1);

		if (hideControlsFlag == -1 || hideControlsFlag == 0) {
			Box.setChecked(false);
			contols = true;
		} else if (hideControlsFlag == 1) {
			Box.setChecked(true);
			contols = false;
		}

		Box.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Box.isChecked()) {
					contols = false;

					Editor editor = Settings.edit();
					editor.putInt(Constants.APP_PREFERENCES_CONTROLS_FLAG, 1);
					editor.apply();
				} else {
					contols = true;
					Editor editor = Settings.edit();
					editor.putInt(Constants.APP_PREFERENCES_CONTROLS_FLAG, 0);
					editor.apply();
				}

			}

		});

		final Button button = (Button) findViewById(R.id.start);
		button.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(View v) {
				try {
					Intent intent = new Intent(context, SDLActivity.class);
					finish();

					context.startActivity(intent);

				} catch (Exception e) {
					 Toast.makeText(getApplicationContext(),
								"Game can not be loaded", Toast.LENGTH_LONG).show();
						
				}

			}

		});

		final Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(View v) {

				try {
					Intent intent = new Intent(context, PluginsView.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
	
				} catch (Exception e) {
					 Toast.makeText(getApplicationContext(),
								"no data files", Toast.LENGTH_LONG).show();
				}
				
			}

		});

		final Button button2 = (Button) findViewById(R.id.buttoncontrols);
		button2.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(View v) {

				Intent intent = new Intent(context, ConfigureControls.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);

			}

		});

		final Button button3 = (Button) findViewById(R.id.buttonresetcontrols);
		button3.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(View v) {
				Editor editor = Settings.edit();
				editor.putInt(Constants.APP_PREFERENCES_RESET_CONTROLS, 1);
				editor.apply();
				Toast toast = Toast.makeText(getApplicationContext(),
						"Reset on-screen controls", Toast.LENGTH_LONG);
				toast.show();

			}

		});

		final Button button4 = (Button) findViewById(R.id.buttoncopy);
		button4.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(View v) {
				linlaHeaderProgress.setVisibility(View.VISIBLE);
				th = new Thread(new Runnable() {

					public Handler UI = new Handler() {
						@Override
						public void dispatchMessage(Message msg) {
							super.dispatchMessage(msg);

							linlaHeaderProgress.setVisibility(View.GONE);
							Toast toast = Toast.makeText(
									getApplicationContext(),
									"files are copied", Toast.LENGTH_LONG);
							toast.show();
						}
					};

					@Override
					public void run() {
						copyFileOrDir("libopenmw");

						UI.sendEmptyMessage(0);
					}
				});
				th.start();

			}

		});

	}

	private void copyFileOrDir(final String path) {

		AssetManager assetManager = context.getAssets();
		String assets[] = null;
		try {
			assets = assetManager.list(path);
			if (assets.length == 0) {
				copyFile(path);
			} else {
				String fullPath = "/sdcard" + "/" + path;
				File dir = new File(fullPath);
				if (!dir.exists())
					dir.mkdir();
				for (int i = 0; i < assets.length; ++i) {
					copyFileOrDir(path + "/" + assets[i]);
				}
			}
		} catch (IOException ex) {
			Log.e("tag", "I/O Exception", ex);
		}

	}

	private void copyFile(String filename) {

		AssetManager assetManager = this.getAssets();

		InputStream in = null;
		OutputStream out = null;
		try {
			in = assetManager.open(filename);
			String newFileName = "/sdcard" + "/" + filename;
			out = new FileOutputStream(newFileName);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;
			out.flush();
			out.close();
			out = null;
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}

	}

}

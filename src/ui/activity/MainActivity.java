package ui.activity;

import java.io.File;

import ui.files.CopyFilesFromAssets;
import ui.files.ParseJson;
import ui.files.Writer;

import com.libopenmw.openmw.R;

import constants.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static boolean contols = true;

	private LinearLayout linlaHeaderProgress;
	private SharedPreferences Settings;

	private Thread th;

	public static String configsPath = "";
	public static String dataPath = "";
	public static String commandLineData="";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
		Settings = getSharedPreferences(Constants.APP_PREFERENCES,
				Context.MODE_MULTI_PROCESS);

		final CheckBox Box = (CheckBox) findViewById(R.id.checkBox1);

		int hideControlsFlag = Settings.getInt(
				Constants.APP_PREFERENCES_CONTROLS_FLAG, -1);

		configsPath = Settings.getString(Constants.CONFIGS_PATH, "");
		commandLineData = Settings.getString(Constants.COMMAND_LINE, "");

		if (configsPath.equals("")) {
			configsPath = Environment.getExternalStorageDirectory()
					+ "/libopenmw";
			Editor editor = Settings.edit();
			editor.putString(Constants.CONFIGS_PATH, configsPath);
			editor.apply();
		}

		dataPath = Settings.getString(Constants.DATA_PATH, "");

		if (dataPath.equals("")) {
			dataPath = Environment.getExternalStorageDirectory()
					+ "/libopenmw/data";
			Editor editor = Settings.edit();
			editor.putString(Constants.DATA_PATH, dataPath);
			editor.apply();
		}

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

		Button startGame = (Button) findViewById(R.id.start);
		startGame.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(View v) {
				try {

					Intent intent = new Intent(MainActivity.this,
							GameActivity.class);
					finish();

					MainActivity.this.startActivity(intent);

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Game can not be loaded", Toast.LENGTH_LONG).show();

				}

			}

		});

		Button pluginsViewButton = (Button) findViewById(R.id.button1);
		pluginsViewButton.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(View v) {

				try {
					Intent intent = new Intent(MainActivity.this,
							PluginsView.class);
					MainActivity.this.startActivity(intent);

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "no data files",
							Toast.LENGTH_LONG).show();
				}

			}

		});

		findViewById(R.id.buttoncontrols).setOnClickListener(
				new View.OnClickListener() {
					@SuppressLint("InlinedApi")
					public void onClick(View v) {

						Intent intent = new Intent(MainActivity.this,
								ConfigureControls.class);
						MainActivity.this.startActivity(intent);

					}

				});

		findViewById(R.id.buttonresetcontrols).setOnClickListener(
				new View.OnClickListener() {
					@SuppressLint("InlinedApi")
					public void onClick(View v) {
						Editor editor = Settings.edit();
						editor.putInt(Constants.APP_PREFERENCES_RESET_CONTROLS,
								1);
						editor.apply();
						Toast toast = Toast.makeText(getApplicationContext(),
								"Reset on-screen controls", Toast.LENGTH_LONG);
						toast.show();

					}

				});

		findViewById(R.id.buttoncopy).setOnClickListener(
				new View.OnClickListener() {
					@SuppressLint("InlinedApi")
					public void onClick(View v) {
						linlaHeaderProgress.setVisibility(View.VISIBLE);
						th = new Thread(new Runnable() {

							public Handler UI = new Handler() {
								@Override
								public void dispatchMessage(Message msg) {
									super.dispatchMessage(msg);

									linlaHeaderProgress
											.setVisibility(View.GONE);
									Toast toast = Toast.makeText(
											getApplicationContext(),
											"files copied", Toast.LENGTH_LONG);
									toast.show();
								}
							};

							@Override
							public void run() {
								File inputfile = new File(
										ParseJson.jsonFilePath);
								if (inputfile.exists())
									inputfile.delete();
								CopyFilesFromAssets copyFiles = new CopyFilesFromAssets(
										MainActivity.this, configsPath);
								copyFiles.copyFileOrDir("libopenmw");

								try {
									Writer.write(
											configsPath + "/resources",
											configsPath
													+ "/config/openmw/openmw.cfg",
											"resources");
									Writer.write(
											MainActivity.dataPath,
											MainActivity.configsPath
													+ "/config/openmw/openmw.cfg",
											"data");
									int pos = Settings.getInt(
											Constants.LANGUAGE, 0);

									Writer.write(
											SettingsActivity.data[pos],
											MainActivity.configsPath
													+ "/config/openmw/openmw.cfg",
											"encoding");

									pos = Settings.getInt(Constants.MIPMAPPING,
											0);

									Writer.write(
											SettingsActivity.dataMipmapping[pos],
											MainActivity.configsPath
													+ "/config/openmw/settings.cfg",
											"texture filtering");
									pos = Settings.getInt(Constants.SUBTITLES,
											-1);
									if (pos == 1)
										Writer.write(
												"true",
												MainActivity.configsPath
														+ "/config/openmw/settings.cfg",
												"subtitles");
									else if (pos == 0)
										Writer.write(
												"false",
												MainActivity.configsPath
														+ "/config/openmw/settings.cfg",
												"subtitles");

								} catch (Exception e) {
								}

								UI.sendEmptyMessage(0);
							}
						});
						th.start();

					}

				});

		final Button settingsButton = (Button) findViewById(R.id.settingsbutton);
		settingsButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						SettingsActivity.class);

				MainActivity.this.startActivity(intent);

			}

		});

	}

}

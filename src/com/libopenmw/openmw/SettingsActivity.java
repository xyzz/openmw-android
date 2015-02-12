package com.libopenmw.openmw;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private SharedPreferences Settings;
	private EditText configsText;
	private EditText dataText;
static	String[] data = { "win1250", "win1251", "win1252" };
static	String[] dataMipmapping = { "none", "trilinear", "bilinear","anisotropic" };

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		configsText = (EditText) findViewById(R.id.configsPath);
		dataText = (EditText) findViewById(R.id.editText1);
		Settings = getSharedPreferences(Constants.APP_PREFERENCES,
				Context.MODE_MULTI_PROCESS);

		configsText.setText(MainActivity.configsPath);
		dataText.setText(MainActivity.dataPath);

		configsText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				saveToSharedPreferences(Constants.CONFIGS_PATH, s.toString());
				MainActivity.configsPath = s.toString();

			}
		});

		dataText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				try {
					Writer.write(s.toString(), MainActivity.configsPath
							+ "/config/openmw/openmw.cfg", "data");

				} catch (Exception e) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"configs files not found", Toast.LENGTH_LONG);
					toast.show();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				saveToSharedPreferences(Constants.DATA_PATH, s.toString());
				MainActivity.dataPath = s.toString();

			}
		});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		spinner.setAdapter(adapter);
		spinner.setPrompt("Encoding");
		spinner.setSelection(Settings.getInt(Constants.LANGUAGE, 0));
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					Writer.write(spinner.getSelectedItem().toString(),
							MainActivity.configsPath
									+ "/config/openmw/openmw.cfg", "encoding");
					Editor editor = Settings.edit();
					editor.putInt(Constants.LANGUAGE, position);
					editor.apply();
				

				} catch (Exception e) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"configs files not found", Toast.LENGTH_LONG);
					toast.show();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		ArrayAdapter<String> adapterMipmapping = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, dataMipmapping);
		adapterMipmapping.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final Spinner spinnerMipmapping = (Spinner) findViewById(R.id.mipmappingspinner);
		spinnerMipmapping.setAdapter(adapterMipmapping);
		spinnerMipmapping.setPrompt("Mipmapping");
		spinnerMipmapping.setSelection(Settings.getInt(Constants.MIPMAPPING, 0));
		spinnerMipmapping.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					Writer.write(spinnerMipmapping.getSelectedItem().toString(),
							MainActivity.configsPath
									+ "/config/openmw/settings.cfg", "texture filtering");
					Editor editor = Settings.edit();
					editor.putInt(Constants.MIPMAPPING, position);
					editor.apply();
				

				} catch (Exception e) {
					Toast toast = Toast.makeText(getApplicationContext(),
							"configs files not found", Toast.LENGTH_LONG);
					toast.show();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		final CheckBox Box = (CheckBox) findViewById(R.id.checkBox1);

		int hideFlag = Settings.getInt(Constants.SUBTITLES, -1);
		if (hideFlag == -1 || hideFlag == 0) {
			Box.setChecked(false);
		} else if (hideFlag == 1) {
			Box.setChecked(true);
		}

		Box.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Box.isChecked()) {

					try {
						Writer.write("true", MainActivity.configsPath
								+ "/config/openmw/settings.cfg", "subtitles");

						Editor editor = Settings.edit();
						editor.putInt(Constants.SUBTITLES, 1);
						editor.apply();
					
					} catch (Exception e) {
						
						Toast toast = Toast.makeText(getApplicationContext(),
								"configs files not found", Toast.LENGTH_LONG);
						toast.show();
					}

				} else {
					try {
						Writer.write("false", MainActivity.configsPath
								+ "/config/openmw/settings.cfg", "subtitles");
						Editor editor = Settings.edit();
						editor.putInt(Constants.SUBTITLES, 0);
						editor.apply();
					
					} catch (Exception e) {
						Toast toast = Toast.makeText(getApplicationContext(),
								"configs files not found", Toast.LENGTH_LONG);
						toast.show();
					}

				}

			}

		});

	}

	public void saveToSharedPreferences(String value, String buffer) {
		Editor editor = Settings.edit();
		editor.putString(value, buffer);
		editor.apply();
	}

}

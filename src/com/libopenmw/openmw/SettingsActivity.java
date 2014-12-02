package com.libopenmw.openmw;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private SharedPreferences Settings;
	private String configsPath = "";
	private String dataPath = "";
	private EditText configsText;
	private EditText dataText;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		configsText = (EditText) findViewById(R.id.configsPath);
		dataText = (EditText) findViewById(R.id.editText1);
		Settings = getSharedPreferences(Constants.APP_PREFERENCES,
				Context.MODE_MULTI_PROCESS);
		configsPath = Settings.getString(Constants.CONFIGS_PATH, "/sdcard");
		dataPath = Settings.getString(Constants.DATA_PATH, "");

		configsText.setText(configsPath);
		dataText.setText(dataPath);

		final Button settingsButton = (Button) findViewById(R.id.buttonsave);
		settingsButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Editor editor = Settings.edit();
				editor.putString(Constants.CONFIGS_PATH, configsText.getText()
						.toString());
				editor.putString(Constants.DATA_PATH, dataText.getText()
						.toString());
				editor.apply();

				Toast.makeText(getApplicationContext(), "Saved",
						Toast.LENGTH_LONG).show();

			}

		});
	}

}

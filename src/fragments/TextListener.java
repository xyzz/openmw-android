package fragments;

import constants.Constants;
import ui.files.PreferencesHelper;
import ui.files.Writer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.Editable;
import android.text.TextWatcher;

public class TextListener implements TextWatcher {

	private SharedPreferences Settings;
	private String data;

	private String value;
	private String sharedprefValue;
	private String gameValue;
	private Activity a;
	private String mode = "";

	public TextListener(Activity a, String data, String value,
			String sharedprefValue, String gameValue,
			SharedPreferences Settings, String mode) {
		this.data = data;

		this.value = value;
		this.sharedprefValue = sharedprefValue;
		this.gameValue = gameValue;
		this.Settings = Settings;
		this.a = a;
		this.mode = mode;
	}

	@Override
	public void afterTextChanged(final Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		saveToSharedPreferences(sharedprefValue, s.toString());
		gameValue = s.toString();
		PreferencesHelper.getPrefValues(a);
		saveData(s.toString());
	}

	private void saveData(final String s) {
		switch (mode) {
		case "configs":
			new Thread(new Runnable() {
				public void run() {
					try {
						Writer.write(s + data, s + "/config/openmw/openmw.cfg", value);

					} catch (Exception e) {

					}

				}
			}).start();
			break;

		case "data":
			new Thread(new Runnable() {
				public void run() {
					try {
						Writer.write(s + data,  Constants.configsPath + "/config/openmw/openmw.cfg", value);

					} catch (Exception e) {

					}

				}
			}).start();
			break;

		default:
			break;
		}

	}

	private void saveToSharedPreferences(String value, String buffer) {
		Editor editor = Settings.edit();
		editor.putString(value, buffer);
		editor.apply();
	}

}
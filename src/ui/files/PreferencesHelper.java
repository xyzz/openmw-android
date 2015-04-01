package ui.files;

import constants.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

public class PreferencesHelper {

	public static void getPrefValues(Activity a) {
		SharedPreferences Settings;
		Settings = a.getSharedPreferences(Constants.APP_PREFERENCES,
				Context.MODE_MULTI_PROCESS);
		Constants.configsPath = Settings.getString(Constants.CONFIGS_PATH,
				Environment.getExternalStorageDirectory() + "/libopenmw");
		Constants.commandLineData = Settings.getString(Constants.COMMAND_LINE,
				"");
		Constants.dataPath = Settings.getString(Constants.DATA_PATH,
				Environment.getExternalStorageDirectory() + "/libopenmw/data");
		Constants.hideControls = Settings.getInt(
				Constants.APP_PREFERENCES_CONTROLS_FLAG, -1);

	}
	
	
	public static void setPreferences(String prefValue,int value,Activity a){
		SharedPreferences Settings;
		Settings = a.getSharedPreferences(Constants.APP_PREFERENCES,
				Context.MODE_MULTI_PROCESS);
		Editor editor = Settings.edit();
		editor.putInt(prefValue, value);		
		editor.apply();

	}

	

}

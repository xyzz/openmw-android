package ui.files;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.libopenmw.openmw.BuildConfig;

import constants.Constants;
import ui.fragments.ScreenResolutionHelper;

/**
 * Created by sandstranger on 07.01.16.
 */
public class ConfigsFileStorageHelper {

    public static final String CONFIGS_FILES_STORAGE_PATH = Environment.getExternalStorageDirectory() + "/Android/data/" + BuildConfig.APPLICATION_ID;
    private MaterialDialog dialog;
    private Activity activity;
    private SharedPreferences Settings;
    private final String FIRST_TIME_RUN_KEY = "first_run";

    public ConfigsFileStorageHelper(Activity activity, SharedPreferences Settings) {
        this.activity = activity;
        this.Settings = Settings;
    }

    public void checkAppFirstTimeRun() {
        if (Settings.getBoolean(FIRST_TIME_RUN_KEY, true)) {
            Settings.edit().putBoolean(FIRST_TIME_RUN_KEY, false).commit();
        }
    }

    private void hideDialog() {
        dialog.dismiss();
        Toast toast = Toast.makeText(activity,
                "files copied to " +CONFIGS_FILES_STORAGE_PATH, Toast.LENGTH_LONG);
        toast.show();
    }

    public void copyFiles() {
        showCopyDialog();
        Thread th = new Thread(new Runnable() {

            @Override
            public void run() {

                CopyFilesFromAssets copyFiles = new CopyFilesFromAssets(
                        activity,
                        CONFIGS_FILES_STORAGE_PATH);
                copyFiles.copyFileOrDir("libopenmw");

                try {

                    file.Writer.write(
                            CONFIGS_FILES_STORAGE_PATH + "/libopenmw/resources",
                            CONFIGS_FILES_STORAGE_PATH + "/config/openmw/openmw.cfg",
                            "libopenmw/resources");
                    file.Writer.write(Constants.APPLICATION_DATA_STORAGE_PATH, CONFIGS_FILES_STORAGE_PATH
                            + "/config/openmw/openmw.cfg", "data");

                    file.Writer.write(
                            PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.LANGUAGE, "win1250"),
                            CONFIGS_FILES_STORAGE_PATH + "/config/openmw/openmw.cfg",
                            "encoding");


                    file.Writer.write(PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.MIPMAPPING, "none"),
                            CONFIGS_FILES_STORAGE_PATH
                                    + "/config/openmw/settings.cfg",
                            "texture filtering");

                    file.Writer.write(String.valueOf(PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(Constants.SUBTITLES, false)), CONFIGS_FILES_STORAGE_PATH
                            + "/config/openmw/settings.cfg", "subtitles");

                    file.Writer.write("" + Settings.getFloat(Constants.CAMERA_MULTIPLISER, 2.0f), CONFIGS_FILES_STORAGE_PATH
                            + "/config/openmw/settings.cfg", Constants.CAMERA_MULTIPLISER);
                    file.Writer.write("" + Settings.getFloat(Constants.TOUCH_SENSITIVITY, 0.01f), CONFIGS_FILES_STORAGE_PATH
                            + "/config/openmw/settings.cfg", Constants.TOUCH_SENSITIVITY);

                    ScreenResolutionHelper screenHelper = new ScreenResolutionHelper(activity);
                    screenHelper.writeScreenResolution(PreferenceManager.getDefaultSharedPreferences(activity).getString(Constants.RESOLUTION, "normalResolution"));


                } catch (Exception e) {
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideDialog();
                    }
                });
            }
        });
        th.start();
    }

    private void showCopyDialog() {
        dialog = new MaterialDialog.Builder(activity)
                .title("Copying config files")
                .content("Please wait")
                .progress(true, 0)
                .show();
    }


}

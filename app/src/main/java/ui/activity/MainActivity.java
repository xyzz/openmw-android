package ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.libopenmw.openmw.R;

import io.fabric.sdk.android.Fabric;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import file.utils.CopyFilesFromAssets;
import mods.Mod;
import mods.ModType;
import mods.ModsCollection;
import mods.ModsDatabaseOpenHelper;
import ui.fragments.FragmentSettings;
import permission.PermissionHelper;
import file.ConfigsFileStorageHelper;

import static file.ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH;
import static file.ConfigsFileStorageHelper.OPENMW_BASE_CFG;
import static file.ConfigsFileStorageHelper.OPENMW_CFG;
import static file.ConfigsFileStorageHelper.SETTINGS_CFG;
import static utils.Utils.hideAndroidControls;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "OpenMW-Launcher";
    private SharedPreferences prefs;

    public static int resolutionX = 0;
    public static int resolutionY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        PermissionHelper.getWriteExternalStoragePermission(MainActivity.this);
        setContentView(R.layout.main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FragmentSettings()).commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> startGame());
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    private void logConfig() {
        try {
            File openmwCfg = new File(OPENMW_CFG);
            if (openmwCfg.exists()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(openmwCfg)));
                String line;
                Crashlytics.log("openmw.cfg");
                Crashlytics.log("--------------------------------------------------------------------------------");
                while ((line = reader.readLine()) != null) {
                    // Don't log fallback lines, they are mostly useless
                    if (!line.contains("fallback="))
                        Crashlytics.log(line);
                }
                Crashlytics.log("--------------------------------------------------------------------------------");
            }
        } catch (Exception e) {
            // not a big deal if we can't log the contents
        }
    }

    private void runGame() {
        logConfig();
        Intent intent = new Intent(MainActivity.this,
                GameActivity.class);
        finish();

        MainActivity.this.startActivity(intent);
    }

    /**
     * Resets $base/config to default values. This contains user-modifiable openmw.cfg and settings.cfg
     * (and we also write some values to both on startup such as screen res or some options)
     */
    private void resetUserConfig() {
        // Wipe out the old version
        deleteRecursive(new File(CONFIGS_FILES_STORAGE_PATH + "/config"));
        // and copy in the default values
        CopyFilesFromAssets copyFiles = new CopyFilesFromAssets(this, CONFIGS_FILES_STORAGE_PATH);
        copyFiles.copyFileOrDir("libopenmw/config");
    }

    private void obtainScreenResolution() {
        View v = getWindow().getDecorView();
        resolutionX = v.getWidth();
        resolutionY = v.getHeight();

        // Split resolution e.g 640x480 to width/height
        String customResolution = prefs.getString("pref_customResolution", "");
        int sep = customResolution.indexOf("x");
        if (sep > 0) {
            try {
                int x = Integer.parseInt(customResolution.substring(0, sep));
                int y = Integer.parseInt(customResolution.substring(sep + 1));

                resolutionX = x;
                resolutionY = y;
            } catch (NumberFormatException e) {
                // pass
            }
        }

        try {
            file.Writer.write(ConfigsFileStorageHelper.SETTINGS_CFG, "resolution x", String.valueOf(resolutionX));
            file.Writer.write(ConfigsFileStorageHelper.SETTINGS_CFG, "resolution y", String.valueOf(resolutionY));
        } catch (IOException e) {
            // TODO
        }
    }

    // https://stackoverflow.com/a/13357785/2606891
    static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    static String readFile(String filePath) throws IOException {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }

    /**
     * Generates openmw.cfg using values from openmw-base.cfg combined with mod manager settings
     */
    private void generateOpenmwCfg() {
        String base = "";
        try {
            base = readFile(OPENMW_BASE_CFG);
        } catch (IOException e) {
            Log.e(TAG, "Failed to read openmw-base.cfg", e);
            Crashlytics.logException(e);
        }

        String dataFiles = prefs.getString("data_files", "");
        ModsDatabaseOpenHelper db = ModsDatabaseOpenHelper.Companion.getInstance(this);
        ModsCollection resources = new ModsCollection(ModType.Resource, dataFiles, db);
        ModsCollection plugins = new ModsCollection(ModType.Plugin, dataFiles, db);

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(OPENMW_CFG), "UTF-8"))) {
            writer.write("# Automatically generated, do not edit\n");

            for (Mod mod : resources.getMods()) {
                if (mod.getEnabled())
                    writer.write("fallback-archive=" + mod.getFilename() + "\n");
            }

            writer.write("\n" + base + "\n");

            for (Mod mod : plugins.getMods()) {
                if (mod.getEnabled())
                    writer.write("content=" + mod.getFilename() + "\n");
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to generate openmw.cfg.", e);
            Crashlytics.logException(e);
        }
    }

    private void startGame() {
        ProgressDialog dialog = ProgressDialog.show(
                this, "", "Preparing for launch...", true);

        Activity activity = this;

        // hide the controls so that ScreenResolutionHelper can get the right resolution
        hideAndroidControls(this);

        Thread th = new Thread(() -> {
            try {
                File openmwBaseCfg = new File(OPENMW_BASE_CFG);
                File settingsCfg = new File(SETTINGS_CFG);
                if (!openmwBaseCfg.exists() || !settingsCfg.exists()) {
                    Log.i(TAG, "Config files don't exist, re-creating them.");
                    resetUserConfig();
                }

                // wipe old "wipeable" (see ConfigsFileStorageHelper) config files just to be safe
                deleteRecursive(new File(CONFIGS_FILES_STORAGE_PATH + "/openmw"));
                deleteRecursive(new File(CONFIGS_FILES_STORAGE_PATH + "/resources"));

                // copy all assets
                CopyFilesFromAssets copyFiles = new CopyFilesFromAssets(activity, CONFIGS_FILES_STORAGE_PATH);
                copyFiles.copyFileOrDir("libopenmw/openmw");
                copyFiles.copyFileOrDir("libopenmw/resources");

                generateOpenmwCfg();

                // openmw.cfg: data, resources
                // TODO: probably should just reuse ConfigsFileStorageHelper
                file.Writer.write(
                        OPENMW_CFG, "resources", CONFIGS_FILES_STORAGE_PATH + "/resources"
                );
                // TODO: it will crash if there's no value/invalid value provided
                file.Writer.write(OPENMW_CFG, "data", prefs.getString("data_files", ""));

                file.Writer.write(OPENMW_CFG, "encoding", prefs.getString("pref_encoding", "win1252"));

                file.Writer.write(SETTINGS_CFG, "scaling factor", prefs.getString("pref_uiScaling", "1.0"));

                file.Writer.write(SETTINGS_CFG, "allow capsule shape", prefs.getString("pref_allowCapsuleShape", "true"));

                file.Writer.write(SETTINGS_CFG, "preload enabled", prefs.getString("pref_preload", "false"));

                runOnUiThread(() -> {
                    obtainScreenResolution();
                    dialog.hide();
                    runGame();
                });
            } catch (IOException e) {
                Log.e(TAG, "Failed to write config files.", e);
                Crashlytics.logException(e);
            }
        });
        th.start();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_reset_config:
                resetUserConfig();
                Toast.makeText(this, getString(R.string.config_was_reset), Toast.LENGTH_SHORT).show();
                break;

            case R.id.action_about:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.about_title))
                        .setMessage(R.string.about_contents)
                        .show();

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}

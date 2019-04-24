package ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.libopenmw.openmw.R;
import com.melnykov.fab.FloatingActionButton;

import io.fabric.sdk.android.Fabric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import constants.Constants;
import file.utils.CopyFilesFromAssets;
import ui.game.GameState;
import ui.fragments.FragmentControls;
import ui.fragments.FragmentSettings;
import permission.PermissionHelper;
import ui.screen.ScreenScaler;
import file.ConfigsFileStorageHelper;
import prefs.PreferencesHelper;

import static file.ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH;
import static file.ConfigsFileStorageHelper.OPENMW_CFG;
import static file.ConfigsFileStorageHelper.SETTINGS_CFG;
import static utils.Utils.hideAndroidControls;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "OpenMW-Launcher";
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    public TextView path;
    public Button browseButton;
    private Menu menu;
    private boolean isSettingsEnabled = true;
    private static final int REQUEST_PATH = 1;
    private SharedPreferences prefs;
    private SharedPreferences Settings;
    private TextListener listener;
    private enum TEXT_MODE {DATA_PATH, COMMAND_LINE}
    private static TEXT_MODE editTextMode;

    public static int resolutionX = 0;
    public static int resolutionY = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        GameState.setGameState(false);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        PermissionHelper.getWriteExternalStoragePermission(MainActivity.this);
        isSettingsEnabled = true;
        setContentView(R.layout.main);
        PreferencesHelper.getPrefValues(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Settings = this.getSharedPreferences(
                Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        LinearLayout layout = (LinearLayout) findViewById(R.id.toolbarLayout);
        layout.setVisibility(LinearLayout.VISIBLE);
        path = (TextView) findViewById(R.id.path);
        browseButton = (Button) findViewById(R.id.buttonBrowse);

        disableToolBarViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FragmentSettings()).commit();

        initializeNavigationView(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GameState.setGameState(true);
                startGame();
            }

        });
    }


    private void initializeNavigationView(Toolbar toolbar) {
        navigationView = (NavigationView) findViewById(R.id.navigation_drawer);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case R.id.start_game:
                        startGame();
                        return true;

//                    case R.id.plugins:
//                        showOverflowMenu(true);
//                        isSettingsEnabled = false;
//                        disableToolBarViews();
//                        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentPlugins()).commit();
//                        return true;
                    case R.id.controls:
                        showOverflowMenu(false);
                        disableToolBarViews();
                        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentControls()).commit();

                        return true;
                    case R.id.settings:
                        disableToolBarViews();
                        showOverflowMenu(true);
                        isSettingsEnabled = true;
                        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentSettings()).commit();

                        return true;
/*                    case R.id.textureDecoder:
                        showOverflowMenu(false);
                        disableToolBarViews();
                        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new DDSDecoderFragment()).commit();
                        return true;*/

                    default:

                        return true;

                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PATH) {
            if (resultCode == Activity.RESULT_OK) {
                String curDir = data.getStringExtra("GetDir");

                switch (editTextMode) {
                    case DATA_PATH:
                        Constants.APPLICATION_DATA_STORAGE_PATH = curDir;
                        break;
                }
                setTexWatcher();
                path.setVisibility(EditText.VISIBLE);
                browseButton.setVisibility(Button.VISIBLE);
                path.setText(curDir);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

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
            file.Writer.write(String.valueOf(resolutionX), ConfigsFileStorageHelper.SETTINGS_CFG, "resolution x");
            file.Writer.write(String.valueOf(resolutionY), ConfigsFileStorageHelper.SETTINGS_CFG, "resolution y");
        } catch (IOException e) {
            // TODO
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
                File openmwCfg = new File(OPENMW_CFG);
                File settingsCfg = new File(SETTINGS_CFG);
                if (!openmwCfg.exists() || !settingsCfg.exists()) {
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

                // openmw.cfg: data, resources
                // TODO: probably should just reuse ConfigsFileStorageHelper
                file.Writer.write(
                        CONFIGS_FILES_STORAGE_PATH + "/resources",
                        OPENMW_CFG,
                        "resources");
                // TODO: it will crash if there's no value/invalid value provided
                file.Writer.write(prefs.getString("data_files", ""), OPENMW_CFG, "data");

                file.Writer.write(prefs.getString("pref_encoding", "win1252"), OPENMW_CFG, "encoding");

                file.Writer.write(prefs.getString("pref_uiScaling", "1.0"), SETTINGS_CFG, "scaling factor");

                file.Writer.write(prefs.getString("pref_allowCapsuleShape", "true"), SETTINGS_CFG, "allow capsule shape");

                file.Writer.write(prefs.getString("pref_preload", "false"), SETTINGS_CFG, "preload enabled");

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

    private void setTexWatcher() {
        switch (editTextMode) {
            case COMMAND_LINE:
                removeTextlistener();
                listener = new TextListener(this
                        , "", "", Constants.COMMAND_LINE,
                        Constants.commandLineData, Settings, "commandLine");
                path.addTextChangedListener(listener);
                break;
            case DATA_PATH:
                removeTextlistener();
                listener = new TextListener(this,
                        "", "data", Constants.DATA_PATH, Constants.APPLICATION_DATA_STORAGE_PATH, Settings,
                        "data");
                path.addTextChangedListener(listener);
                break;
            default:
                break;
        }
    }


    public void showOverflowMenu(boolean showMenu) {
        if (menu == null)
            return;
        menu.setGroupVisible(R.id.main_menu_group, showMenu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        if (isSettingsEnabled)
            inflater.inflate(R.menu.menu_settings, menu);
        else
            inflater.inflate(R.menu.menu_plugins, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (!isSettingsEnabled)
            switch (id) {
                default:
                    break;
            }
        else
            switch (id) {
                case R.id.action_show_screen_controls:
                    startControlsActivity();
                    break;

                case R.id.action_reset_config:
                    resetUserConfig();
                    Toast.makeText(this, getString(R.string.config_was_reset), Toast.LENGTH_SHORT).show();
                    break;

                default:
                    break;
            }

        return super.onOptionsItemSelected(item);
    }

    private void startControlsActivity() {
        Intent intent = new Intent(this,
                ConfigureControls.class);
        this.startActivity(intent);

    }

    private void enableToolbarViews() {
        path.setVisibility(EditText.VISIBLE);
        browseButton.setVisibility(Button.VISIBLE);
        setTexWatcher();
    }

    private void disableToolBarViews() {
        browseButton.setVisibility(Button.GONE);
        path.setVisibility(EditText.GONE);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ScreenScaler.textScaler(path, 3f);
        ScreenScaler.textScaler(browseButton, 4.8f);
    }

    private void removeTextlistener() {
        if (listener != null)
            path.removeTextChangedListener(listener);


    }

}




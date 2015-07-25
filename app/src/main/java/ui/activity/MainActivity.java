package ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.libopenmw.openmw.FileChooser;
import com.libopenmw.openmw.R;
import com.melnykov.fab.FloatingActionButton;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import constants.Constants;
import fragments.FragmentControls;
import fragments.FragmentPlugins;
import fragments.FragmentSettings;
import fragments.ScreenResolutionHelper;
import screen.ScreenScaler;
import ui.files.CopyFilesFromAssets;
import ui.files.PreferencesHelper;
import ui.files.Writer;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    public TextView path;
    public Button browseButton;
    private Menu menu;
    private boolean isSettingsEnabled = true;
    private static final int REQUEST_PATH = 1;
    private SharedPreferences Settings;
    private TextListener listener;

    private enum TEXT_MODE {DATA_PATH, COMMAND_LINE, CONGIGS_PATH}

    private static TEXT_MODE editTextMode;
    private MaterialDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        isSettingsEnabled = true;
        setContentView(R.layout.main);
        PreferencesHelper.getPrefValues(this);
        Settings = this.getSharedPreferences(
                Constants.APP_PREFERENCES, Context.MODE_PRIVATE);

        LinearLayout layout = (LinearLayout) findViewById(R.id.toolbarLayout);
        layout.setVisibility(LinearLayout.VISIBLE);
        path = (TextView) findViewById(R.id.path);
        browseButton = (Button) findViewById(R.id.buttonBrowse);

        disableToolBarViews();
        browseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser.isDirMode = true;
                getFolder();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new FragmentSettings()).commit();

        initializeNavigationView(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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

                    case R.id.plugins:
                        showOverflowMenu(true);
                        isSettingsEnabled = false;
                        disableToolBarViews();
                        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentPlugins()).commit();
                        return true;
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

    public void getFolder() {
        Intent intent = new Intent(this, FileChooser.class);
        startActivityForResult(intent, REQUEST_PATH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PATH) {
            if (resultCode == Activity.RESULT_OK) {
                String curDir = data.getStringExtra("GetDir");

                switch (editTextMode) {
                    case CONGIGS_PATH:
                        Constants.configsPath = curDir;
                        break;
                    case DATA_PATH:
                        Constants.dataPath = curDir;
                }
                setTexWatcher();
                path.setVisibility(EditText.VISIBLE);
                browseButton.setVisibility(Button.VISIBLE);
                path.setText(curDir);

            }
        }
    }


    private void startGame() {

        Intent intent = new Intent(MainActivity.this,
                GameActivity.class);
        finish();

        MainActivity.this.startActivity(intent);
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
            case CONGIGS_PATH:
                removeTextlistener();
                listener = new TextListener(this,
                        "/resources", "resources", Constants.CONFIGS_PATH,
                        Constants.configsPath, Settings, "configs");
                path.addTextChangedListener(listener);
                break;
            case DATA_PATH:
                removeTextlistener();
                listener = new TextListener(this,
                        "", "data", Constants.DATA_PATH, Constants.dataPath, Settings,
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
                case R.id.action_enable:
                    FragmentPlugins.instance.enableMods();
                    break;
                case R.id.action_disable:
                    FragmentPlugins.instance.disableMods();
                    break;
                case R.id.action_importMods:
                    FragmentPlugins.instance.importMods();
                    break;
                case R.id.action_export:
                    FragmentPlugins.instance.exportMods();
                    break;
                default:
                    break;
            }
        else
            switch (id) {
                case R.id.action_command_line:
                    editTextMode = TEXT_MODE.COMMAND_LINE;
                    enableToolbarViews();
                    browseButton.setVisibility(Button.GONE);
                    path.setText(Constants.commandLineData);
                    break;

                case R.id.action_browse_configs_path:
                    editTextMode = TEXT_MODE.CONGIGS_PATH;
                    enableToolbarViews();
                    path.setText(Constants.configsPath);
                    enableToolbarViews();
                    break;
                case R.id.action_browse_data_path:
                    editTextMode = TEXT_MODE.DATA_PATH;
                    enableToolbarViews();
                    path.setText(Constants.dataPath);
                    break;
                case R.id.action_copy_files:
                    copyFiles();
                    break;

                case R.id.action_reset_screen_controls:
                    resetScreenControls();
                    break;
                case R.id.action_show_screen_controls:
                    startControlsActivity();
                    break;

                default:
                    break;


            }


        return super.onOptionsItemSelected(item);
    }

    private void resetScreenControls() {
        PreferencesHelper.setPreferences(
                Constants.APP_PREFERENCES_RESET_CONTROLS, 1,
                this);
        Toast toast = Toast.makeText(this.
                        getApplicationContext(),
                "Reset on-screen controls", Toast.LENGTH_LONG);
        toast.show();
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


    private void copyFiles() {
        showCopyDialog();
        Thread th = new Thread(new Runnable() {

            public Handler UI = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);
                    dialog.dismiss();
                    Toast toast = Toast.makeText(MainActivity.this.getApplicationContext(),
                            "files copied", Toast.LENGTH_LONG);
                    toast.show();
                }
            };

            @Override
            public void run() {

                CopyFilesFromAssets copyFiles = new CopyFilesFromAssets(
                        MainActivity.this,
                        Constants.configsPath);
                copyFiles.copyFileOrDir("libopenmw");

                try {

                    Writer.write(
                            Constants.configsPath + "/resources",
                            Constants.configsPath + "/config/openmw/openmw.cfg",
                            "resources");
                    Writer.write(Constants.dataPath, Constants.configsPath
                            + "/config/openmw/openmw.cfg", "data");

                    Writer.write(
                            PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString(Constants.LANGUAGE, "win1250"),
                            Constants.configsPath + "/config/openmw/openmw.cfg",
                            "encoding");


                    Writer.write(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString(Constants.MIPMAPPING, "none"),
                            Constants.configsPath
                                    + "/config/openmw/settings.cfg",
                            "texture filtering");

                    Writer.write(String.valueOf(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(Constants.SUBTITLES, false)), Constants.configsPath
                            + "/config/openmw/settings.cfg", "subtitles");

                    Writer.write("" + Settings.getFloat(Constants.CAMERA_MULTIPLISER, 2.0f), Constants.configsPath
                            + "/config/openmw/settings.cfg", Constants.CAMERA_MULTIPLISER);
                    Writer.write("" + Settings.getFloat(Constants.TOUCH_SENSITIVITY, 0.01f), Constants.configsPath
                            + "/config/openmw/settings.cfg", Constants.TOUCH_SENSITIVITY);

                    ScreenResolutionHelper screenHelper = new ScreenResolutionHelper(MainActivity.this);
                    screenHelper.writeScreenResolution(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString(Constants.RESOLUTION, "normalResolution"));


                } catch (Exception e) {
                }

                UI.sendEmptyMessage(0);
            }
        });
        th.start();
    }

    private void showCopyDialog() {
        dialog = new MaterialDialog.Builder(this)
                .title("Copying config files")
                .content("Please wait")
                .progress(true, 0)
                .show();
    }

}




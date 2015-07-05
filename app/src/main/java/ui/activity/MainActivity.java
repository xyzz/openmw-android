package ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.libopenmw.openmw.FileChooser;
import com.libopenmw.openmw.R;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import constants.Constants;
import fragments.FragmentControls;
import fragments.FragmentPlugins;
import fragments.FragmentSettings;
import fragments.ScreenResolutionHelper;
import screen.ScreenScaler;
import ui.files.CopyFilesFromAssets;
import ui.files.PreferencesHelper;
import ui.files.Writer;

public class MainActivity extends ActionBarActivity {

    private Drawer.Result drawerResult = null;
    public TextView path;
    public Button browseButton;
    private Menu menu;
    private boolean isSettingsEnabled = true;
    private static final int REQUEST_PATH = 1;
    private SharedPreferences Settings;
    private TextListener listener;

    private enum TEXT_MODE {DATA_PATH, COMMAND_LINE, CONGIGS_PATH};
    private LinearLayout linlaHeaderProgress;
    private static TEXT_MODE editTextMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        isSettingsEnabled = true;
        setContentView(R.layout.main);
        PreferencesHelper.getPrefValues(this);
        Settings = this.getSharedPreferences(
                Constants.APP_PREFERENCES, Context.MODE_MULTI_PROCESS);

        LinearLayout layout = (LinearLayout) findViewById(R.id.toolbarLayout);
        linlaHeaderProgress = (LinearLayout)
                findViewById(R.id.linlaHeaderProgress);
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
        initializeDrawer(toolbar);
        getFragmentManager().beginTransaction()
                .replace(R.id.frame_container, new FragmentSettings()).commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startGame();
            }

        });


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
                path.setText(curDir);
                switch (editTextMode) {
                    case CONGIGS_PATH:
                        Constants.configsPath = curDir;
                        break;
                    case DATA_PATH:
                        Constants.dataPath = curDir;
                }
            }
        }
    }


    private void initializeDrawer(final Toolbar toolbar) {
        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Start game").withIcon(FontAwesome.Icon.faw_play).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Plugins").withIcon(FontAwesome.Icon.faw_file_archive_o).withIdentifier(2),
                        new SectionDrawerItem().withName("Settings"),
                        new SecondaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_home).withIdentifier(3),
                        new SecondaryDrawerItem().withName("Controls").withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(4)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            switch (drawerItem.getIdentifier()) {
                                case 1:
                                    startGame();
                                    break;

                                case 3:
                                    disableToolBarViews();
                                    showOverflowMenu(true);
                                    isSettingsEnabled = true;
                                    MainActivity.this.getFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentSettings()).commit();
                                    break;
                                case 4:
                                    showOverflowMenu(false);
                                    disableToolBarViews();
                                    MainActivity.this.getFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentControls()).commit();
                                    break;
                                case 2:
                                    showOverflowMenu(true);
                                    isSettingsEnabled = false;
                                    disableToolBarViews();
                                    MainActivity.this.getFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentPlugins()).commit();
                                    break;
                                default:
                                    break;


                            }
                        }

                    }
                })
                .build();

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

    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
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
        ScreenScaler.textScaler(browseButton, 4f);
    }

    private void removeTextlistener() {
        if (listener != null)
            path.removeTextChangedListener(listener);


    }


    private void copyFiles() {
        linlaHeaderProgress.setVisibility(View.VISIBLE);
        Thread th = new Thread(new Runnable() {

            public Handler UI = new Handler() {
                @Override
                public void dispatchMessage(Message msg) {
                    super.dispatchMessage(msg);

                    linlaHeaderProgress.setVisibility(View.GONE);
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

                    Writer.write(String.valueOf( PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean(Constants.SUBTITLES, false)), Constants.configsPath
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


}




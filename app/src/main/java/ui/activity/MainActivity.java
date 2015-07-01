package ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;

import com.libopenmw.openmw.R;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import constants.Constants;
import fragments.FragmentControls;
import fragments.FragmentGeneral;
import fragments.FragmentGraphics;
import fragments.FragmentPlugins;
import fragments.FragmentSettings;
import screen.ScreenScaler;
import ui.files.PreferencesHelper;

public class MainActivity extends ActionBarActivity {

    private Drawer.Result drawerResult = null;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        PreferencesHelper.getPrefValues(this);
        if (Constants.hideControls == -1 || Constants.hideControls == 0) {
            Constants.contols = true;
        } else if (Constants.hideControls == 1) {
            Constants.contols = false;
        }

        Button buttonStartGame;
        buttonStartGame = (Button) findViewById(R.id.button_start_game);
        ScreenScaler.changeTextSize(buttonStartGame, 2.5f);
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startGame();
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("General");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeDrawer(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentGeneral()).commit();


    }


    private void initializeDrawer(final Toolbar toolbar) {
        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Start game").withIcon(FontAwesome.Icon.faw_play).withIdentifier(1),
                        new PrimaryDrawerItem().withName("General").withIcon(FontAwesome.Icon.faw_home).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Screen Controls").withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3),
                        new SectionDrawerItem().withName("Settings"),
                        new SecondaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_anchor).withIdentifier(4),
                        new SecondaryDrawerItem().withName("Graphics").withIcon(FontAwesome.Icon.faw_desktop).withIdentifier(5),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Plugins").withIcon(FontAwesome.Icon.faw_file_archive_o).withIdentifier(6)
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
                                case 2:
                                    toolbar.setTitle("General");
                                    showOverflowMenu(false);
                                    MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentGeneral()).commit();
                                    break;
                                case 3:
                                    toolbar.setTitle("Screen Controls");
                                    showOverflowMenu(false);
                                    MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentControls()).commit();
                                    break;

                                case 4:
                                    toolbar.setTitle("Settings");
                                    showOverflowMenu(false);
                                    MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentSettings()).commit();
                                    break;
                                case 5:
                                    toolbar.setTitle("Graphics");
                                    showOverflowMenu(false);
                                    MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentGraphics()).commit();
                                    break;
                                case 6:
                                    toolbar.setTitle("Plugins");
                                    showOverflowMenu(true);
                                    MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new FragmentPlugins()).commit();
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


    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        showOverflowMenu(false);
        return true;
    }

    public void showOverflowMenu(boolean showMenu) {
        if (menu == null)
            return;
        menu.setGroupVisible(R.id.main_menu_group, showMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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

        return super.onOptionsItemSelected(item);
    }


}




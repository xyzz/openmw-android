package ui.activity;

import java.io.File;

import screen.ScreenScaler;
import tabs.MyTabFactory;
import tabs.TabsPagerAdapter;
import ui.files.CopyFilesFromAssets;
import ui.files.ParseJson;
import ui.files.PreferencesHelper;
import ui.files.Writer;

import com.libopenmw.openmw.R;

import constants.Constants;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends FragmentActivity implements
		OnTabChangeListener, OnPageChangeListener {


	private TabsPagerAdapter mAdapter;

	private ViewPager mViewPager;

	private TabHost mTabHost;

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
		mViewPager = (ViewPager) findViewById(R.id.viewpager);

		initialiseTabHost();

		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		mViewPager.setAdapter(mAdapter);

		mViewPager.setOnPageChangeListener(MainActivity.this);
		Button startGame = (Button) findViewById(R.id.start);
		ScreenScaler.changeTextSize(startGame, 2.3f);
		startGame.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {

					Intent intent = new Intent(MainActivity.this,
							GameActivity.class);
					finish();

					MainActivity.this.startActivity(intent);

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"Game can not be loaded", Toast.LENGTH_LONG).show();

				}

			}

		});

	}

	private static void AddTab(MainActivity activity, TabHost tabHost,
			TabHost.TabSpec tabSpec) {

		tabSpec.setContent(new MyTabFactory(activity));

		tabHost.addTab(tabSpec);

	}

	public void onTabChanged(String tag) {

		int pos = this.mTabHost.getCurrentTab();

		this.mViewPager.setCurrentItem(pos);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

		int pos = this.mViewPager.getCurrentItem();

		this.mTabHost.setCurrentTab(pos);

	}

	@Override
	public void onPageSelected(int arg0) {

	}

	private void initialiseTabHost() {

		mTabHost = (TabHost) findViewById(android.R.id.tabhost);

		mTabHost.setup();

		MainActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("General").setIndicator("General"));
		MainActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Controls").setIndicator("Controls"));
		MainActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Settings").setIndicator("Settings"));
		MainActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Plugins").setIndicator("Plugins"));
		MainActivity.AddTab(this, this.mTabHost,
				this.mTabHost.newTabSpec("Graphics").setIndicator("Graphics"));


		mTabHost.setOnTabChangedListener(this);

	}

}

package com.libopenmw.openmw;

import org.libsdl.app.SDLActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends Activity {

	public Context context;
	public static boolean contols = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		context = this;
		final CheckBox Box = (CheckBox) findViewById(R.id.checkBox1);

		Box.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Box.isChecked()) {
					contols = false;
				} else {
					contols = true;
				}

			}

		});

		final Button button = (Button) findViewById(R.id.start);
		button.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(View v) {
				Intent intent = new Intent(context, SDLActivity.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				finish();

			}

		});

		final Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(View v) {
				
				Intent intent = new Intent(context, PluginsView.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);

			}

		});
		
		final Button button2 = (Button) findViewById(R.id.buttoncontrols);
		button2.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi")
			public void onClick(View v) {
				
				Intent intent = new Intent(context, ConfigureControls.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);

			}

		});

	}

}

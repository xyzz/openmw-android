package com.libopenmw.openmw;

import org.libsdl.app.SDLActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	public Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		context = this;
		final Button button = (Button) findViewById(R.id.start);
		button.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("InlinedApi") public void onClick(View v) {
				Intent intent = new Intent(context, SDLActivity.class);

				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(intent);
			//	finish();

			}

		});
	}

}

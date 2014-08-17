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
	public static boolean contols=true;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		context = this;
		final CheckBox Box=(CheckBox) findViewById(R.id.checkBox1);
	
	
		
		Box.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(Box.isChecked()){
					contols=false;
                }else{
                	contols=true;
                }

			}

		});
	
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

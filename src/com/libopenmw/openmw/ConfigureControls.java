package com.libopenmw.openmw;

import org.libsdl.app.SDLActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class ConfigureControls extends Activity {

	public Context context;
	public static boolean contols = true;

	@SuppressLint("NewApi") @Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.controls);

		
		context = this;
		final MultiTouchListener touchListener=new MultiTouchListener(this);
		final Controls touch = (Controls) findViewById(R.id.controls1);
		touch.setOnTouchListener(touchListener);
		
		touch.setAlpha((float) 1);
		final ImageButton buttonRun = (ImageButton) findViewById(R.id.buttonrun);
		
	  
	
	
		
		
		
		MarginLayoutParams marginParams = new MarginLayoutParams(buttonRun.getLayoutParams());   
       
		marginParams.height=200;
		marginParams.width=200;
	//	marginParams.x=100;
	//	marginParams.y=100;
		   marginParams.setMargins((int)(1000), (int)(100),0, 0);
           
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
		
		buttonRun.setLayoutParams(layoutParams); 
		
		
		
		buttonRun.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:

					buttonRun.setOnTouchListener(touchListener);
					return true; 
									// event
				case MotionEvent.ACTION_UP:
					
					return true; 
				}
				return false;
			}
		});
		
		
			
		//lContainerLayout.addView(buttonRun);
		
		
		
		
		//setContentView(lContainerLayout );	
		
	}

}

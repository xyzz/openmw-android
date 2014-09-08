package com.libopenmw.openmw;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class MultiTouchListener implements OnTouchListener
{

private float mPrevX;
private float mPrevY;
public static int x=0;
public static int y=0;

public ConfigureControls Activity;
public MultiTouchListener(ConfigureControls configureControls) {
    Activity = configureControls;
}

@Override
public boolean onTouch(View view, MotionEvent event) {
    float currX,currY;
    int action = event.getAction();
    switch (action ) {
        case MotionEvent.ACTION_DOWN: {

            mPrevX = event.getX();
            mPrevY = event.getY();
            break;
        }

        case MotionEvent.ACTION_MOVE:
        {

                currX = event.getRawX();
                currY = event.getRawY();


                MarginLayoutParams marginParams = new MarginLayoutParams(view.getLayoutParams());   
                marginParams.setMargins((int)(currX - mPrevX), (int)(currY - mPrevY),0, 0);
                   RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(marginParams);
                view.setLayoutParams(layoutParams); 

                

                x=(int)(currX - mPrevX);
                y=(int)(currY - mPrevY);
                		
            break;
        }



        case MotionEvent.ACTION_CANCEL:
            break;

        case MotionEvent.ACTION_UP:
        	ConfigureControls.buttonFlag=view.getId();
        	if (view.getId()==1)
        		ConfigureControls.button.setText("buttonRun");
            break;
    }

    return true;
}

}
package com.libopenmw.openmw;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class ControlsParams {

public static RelativeLayout.LayoutParams coordinates (View v,int x,int y,int width,int height){
		
		MarginLayoutParams marginParamsRun = new MarginLayoutParams(
				v.getLayoutParams());
		marginParamsRun.height = CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(height);
		marginParamsRun.width = CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(width);
		marginParamsRun
				.setMargins((int) (CoordinatesAllScreens.getInstance()
						.getScaledCoordinateX(x)), (int) (CoordinatesAllScreens
						.getInstance().getScaledCoordinateY(y)), 0, 0);
		RelativeLayout.LayoutParams layoutParamsRun = new RelativeLayout.LayoutParams(
				marginParamsRun); 
		return layoutParamsRun;
		
	}

}

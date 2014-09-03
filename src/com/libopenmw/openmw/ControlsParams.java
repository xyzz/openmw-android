package com.libopenmw.openmw;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class ControlsParams {

public static RelativeLayout.LayoutParams coordinates (View v,int x,int y,int width,int height){
		
		MarginLayoutParams marginParamsRun = new MarginLayoutParams(
				v.getLayoutParams());
		marginParamsRun.height = CoordinateHelper.getInstance()
				.getScaledCoordinateY(height);
		marginParamsRun.width = CoordinateHelper.getInstance()
				.getScaledCoordinateX(width);
		marginParamsRun
				.setMargins((int) (CoordinateHelper.getInstance()
						.getScaledCoordinateX(x)), (int) (CoordinateHelper
						.getInstance().getScaledCoordinateY(y)), 0, 0);
		RelativeLayout.LayoutParams layoutParamsRun = new RelativeLayout.LayoutParams(
				marginParamsRun); 
		return layoutParamsRun;
		
	}

}

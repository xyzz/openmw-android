package com.libopenmw.openmw;

import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class ControlsParams {

public static RelativeLayout.LayoutParams coordinates (View v,int x,int y,int width,int height){
		
		MarginLayoutParams marginParams = new MarginLayoutParams(
				v.getLayoutParams());
		
		marginParams.height = CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(height);
		marginParams.width = CoordinatesAllScreens.getInstance()
				.getScaledCoordinateX(width);
		marginParams
				.setMargins((int) (CoordinatesAllScreens.getInstance()
						.getScaledCoordinateX(x)), (int) (CoordinatesAllScreens
						.getInstance().getScaledCoordinateY(y)), 0, 0);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				marginParams); 
		
		return layoutParams;
		
	}

public static RelativeLayout.LayoutParams coordinatesConfigureControls (View v,int x,int y,int width,int height){
	
	MarginLayoutParams marginParams = new MarginLayoutParams(
			v.getLayoutParams());
	
	marginParams.height = height;
	marginParams.width = width;
	marginParams
			.setMargins((int) x, (int) y, 0, 0);
	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
			marginParams); 
	
	return layoutParams;
	
}




}

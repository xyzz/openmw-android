package ui.controls;

import ui.screen.ScreenScaler;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;

public class ControlsParams {

public static RelativeLayout.LayoutParams coordinates (View v,int x,int y,int width,int height){
		
		MarginLayoutParams marginParams = new MarginLayoutParams(
				v.getLayoutParams());
		
		marginParams.height = ScreenScaler.getInstance()
				.getScaledCoordinateX(height);
		marginParams.width = ScreenScaler.getInstance()
				.getScaledCoordinateX(width);
		marginParams
				.setMargins(ScreenScaler.getInstance()
						.getScaledCoordinateX(x), ScreenScaler
						.getInstance().getScaledCoordinateY(y), 0, 0);
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
			.setMargins(x, y, 0, 0);
	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
			marginParams); 
	
	return layoutParams;
	
}




}

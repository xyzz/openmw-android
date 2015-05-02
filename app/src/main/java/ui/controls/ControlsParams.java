package ui.controls;

import screen.ScreenScaler;
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
				.setMargins((int) (ScreenScaler.getInstance()
						.getScaledCoordinateX(x)), (int) (ScreenScaler
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

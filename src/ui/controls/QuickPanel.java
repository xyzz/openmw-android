package ui.controls;


import com.libopenmw.openmw.R;
import android.app.Activity;
import android.widget.Button;

public class QuickPanel {

	private Activity a;
	public QuickPanel(Activity a) {
		super();
		this.a=a;
		
	}
	public void createQuickPanel(){
		Button f1= (Button) a.findViewById(R.id.F1);
	}
}

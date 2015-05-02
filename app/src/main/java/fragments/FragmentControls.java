package fragments;

import screen.ScreenScaler;
import ui.activity.ConfigureControls;
import ui.files.PreferencesHelper;

import com.libopenmw.openmw.R;

import constants.Constants;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class FragmentControls extends Fragment {

	private CheckBox Box;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		View rootView = inflater.inflate(R.layout.controls, container, false);

		PreferencesHelper.getPrefValues(this.getActivity());

		Box = (CheckBox) rootView.findViewById(R.id.checkBox1);
		ScreenScaler.changeTextSize(Box, 2.2f);

		enableCheckBox();

		Box.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Box.isChecked()) {
					Constants.contols = false;
					PreferencesHelper.setPreferences(
							Constants.APP_PREFERENCES_CONTROLS_FLAG, 1,
							FragmentControls.this.getActivity());

				} else {
					Constants.contols = true;
					PreferencesHelper.setPreferences(
							Constants.APP_PREFERENCES_CONTROLS_FLAG, 0,
							FragmentControls.this.getActivity());
				}

			}

		});

		Button buttonControls = (Button) rootView
				.findViewById(R.id.buttoncontrols);
		ScreenScaler.changeTextSize(buttonControls, 2.2f);
		buttonControls.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(FragmentControls.this.getActivity(),
						ConfigureControls.class);
				FragmentControls.this.getActivity().startActivity(intent);

			}

		});

		Button buttonResetControls = (Button) rootView
				.findViewById(R.id.buttonresetcontrols);
		ScreenScaler.changeTextSize(buttonResetControls, 2.2f);

		buttonResetControls.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				PreferencesHelper.setPreferences(
						Constants.APP_PREFERENCES_RESET_CONTROLS, 1,
						FragmentControls.this.getActivity());
				Toast toast = Toast.makeText(FragmentControls.this
						.getActivity().getApplicationContext(),
						"Reset on-screen controls", Toast.LENGTH_LONG);
				toast.show();

			}

		});

		return rootView;
	}

	private void enableCheckBox() {
		if (Constants.hideControls == -1 || Constants.hideControls == 0) {
			Box.setChecked(false);
			Constants.contols = true;
		} else if (Constants.hideControls == 1) {
			Box.setChecked(true);
			Constants.contols = false;
		}
	}

}

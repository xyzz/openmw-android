package fragments;

import screen.ScreenScaler;
import ui.files.PreferencesHelper;
import ui.files.Writer;

import com.libopenmw.openmw.R;

import constants.Constants;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class FragmentSettings extends Fragment {

	private SharedPreferences Settings;
	public static String[] data = { "win1250", "win1251", "win1252" };
	public static String[] dataMipmapping = { "none", "trilinear", "bilinear",
			"anisotropic" };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Settings = this.getActivity().getSharedPreferences(
				Constants.APP_PREFERENCES, Context.MODE_MULTI_PROCESS);
		View rootView = inflater.inflate(R.layout.settings, container, false);

		PreferencesHelper.getPrefValues(this.getActivity());
		ScreenScaler
		.changeTextSize(rootView.findViewById(R.id.textView2), 4f);
		ScreenScaler
		.changeTextSize(rootView.findViewById(R.id.textView1), 2.4f);
	ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner1);

		spinner.setAdapter(adapter);
		spinner.setPrompt("Encoding");
		spinner.setSelection(Settings.getInt(Constants.LANGUAGE, 0));
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					Writer.write(
							spinner.getSelectedItem().toString(),
							Constants.configsPath + "/config/openmw/openmw.cfg",
							"encoding");
					Editor editor = Settings.edit();
					editor.putInt(Constants.LANGUAGE, position);
					editor.apply();

				} catch (Exception e) {
					Toast toast = Toast.makeText(FragmentSettings.this
							.getActivity().getApplicationContext(),
							"configs files not found", Toast.LENGTH_LONG);
					toast.show();
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		ArrayAdapter<String> adapterMipmapping = new ArrayAdapter<String>(
				this.getActivity(), android.R.layout.simple_spinner_item,
				dataMipmapping);
		adapterMipmapping
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		final Spinner spinnerMipmapping = (Spinner) rootView
				.findViewById(R.id.mipmappingspinner);
		spinnerMipmapping.setAdapter(adapterMipmapping);
		spinnerMipmapping.setPrompt("Mipmapping");

		spinnerMipmapping
				.setSelection(Settings.getInt(Constants.MIPMAPPING, 0));
		spinnerMipmapping
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						try {
							Writer.write(spinnerMipmapping.getSelectedItem()
									.toString(), Constants.configsPath
									+ "/config/openmw/settings.cfg",
									"texture filtering");
							Editor editor = Settings.edit();
							editor.putInt(Constants.MIPMAPPING, position);
							editor.apply();

						} catch (Exception e) {
							Toast toast = Toast.makeText(FragmentSettings.this
									.getActivity().getApplicationContext(),
									"configs files not found",
									Toast.LENGTH_LONG);
							toast.show();
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});

		final CheckBox Box = (CheckBox) rootView.findViewById(R.id.checkBox1);

		ScreenScaler.changeTextSize(Box, 2.2f);

		int hideFlag = Settings.getInt(Constants.SUBTITLES, -1);
		if (hideFlag == -1 || hideFlag == 0) {
			Box.setChecked(false);
		} else if (hideFlag == 1) {
			Box.setChecked(true);
		}

		Box.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (Box.isChecked()) {

					try {
						Writer.write("true", Constants.configsPath
								+ "/config/openmw/settings.cfg", "subtitles");

						Editor editor = Settings.edit();
						editor.putInt(Constants.SUBTITLES, 1);
						editor.apply();

					} catch (Exception e) {

						Toast toast = Toast.makeText(FragmentSettings.this
								.getActivity().getApplicationContext(),
								"configs files not found", Toast.LENGTH_LONG);
						toast.show();
					}

				} else {
					try {
						Writer.write("false", Constants.configsPath
								+ "/config/openmw/settings.cfg", "subtitles");
						Editor editor = Settings.edit();
						editor.putInt(Constants.SUBTITLES, 0);
						editor.apply();

					} catch (Exception e) {
						Toast toast = Toast.makeText(FragmentSettings.this
								.getActivity().getApplicationContext(),
								"configs files not found", Toast.LENGTH_LONG);
						toast.show();
					}

				}

			}

		});

		return rootView;
	}

}

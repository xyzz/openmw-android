package fragments;

import java.io.File;

import screen.ScreenScaler;
import ui.files.CopyFilesFromAssets;
import ui.files.ParseJson;
import ui.files.PreferencesHelper;
import ui.files.Writer;

import com.libopenmw.openmw.R;

import constants.Constants;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FragmentGeneral extends Fragment {

	private EditText configsText;
	private EditText dataText;
	private EditText commandLineText;
	private SharedPreferences Settings;
	private LinearLayout linlaHeaderProgress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		View rootView = inflater.inflate(R.layout.general, container, false);

		PreferencesHelper.getPrefValues(this.getActivity());
		Button buttonCopyFIles;
		buttonCopyFIles = (Button) rootView.findViewById(R.id.buttoncopy);

		configsText = (EditText) rootView.findViewById(R.id.configsPath);
		dataText = (EditText) rootView.findViewById(R.id.editText1);
		commandLineText = (EditText) rootView.findViewById(R.id.commandLine);

		ScreenScaler.changeTextSize(configsText, 2f);
		ScreenScaler.changeTextSize(dataText, 2f);
		ScreenScaler.changeTextSize(commandLineText, 2f);
		ScreenScaler.changeTextSize(buttonCopyFIles, 2.3f);
		ScreenScaler
				.changeTextSize(rootView.findViewById(R.id.textView5), 2.5f);
		ScreenScaler
				.changeTextSize(rootView.findViewById(R.id.textView2), 2.5f);
		ScreenScaler
				.changeTextSize(rootView.findViewById(R.id.textView1), 2.5f);

		linlaHeaderProgress = (LinearLayout) rootView
				.findViewById(R.id.linlaHeaderProgress);
		Settings = this.getActivity().getSharedPreferences(
				Constants.APP_PREFERENCES, Context.MODE_MULTI_PROCESS);

		configsText.setText(Constants.configsPath);
		dataText.setText(Constants.dataPath);
		commandLineText.setText(Constants.commandLineData);

		addTexWatchers();

		buttonCopyFIles.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				copyFiles();

			}

		});

		return rootView;
	}

	private void copyFiles() {
		linlaHeaderProgress.setVisibility(View.VISIBLE);
		Thread th = new Thread(new Runnable() {

			public Handler UI = new Handler() {
				@Override
				public void dispatchMessage(Message msg) {
					super.dispatchMessage(msg);

					linlaHeaderProgress.setVisibility(View.GONE);
					Toast toast = Toast.makeText(FragmentGeneral.this
							.getActivity().getApplicationContext(),
							"files copied", Toast.LENGTH_LONG);
					toast.show();
				}
			};

			@Override
			public void run() {
				File inputfile = new File(ParseJson.jsonFilePath);
				if (inputfile.exists())
					inputfile.delete();
				CopyFilesFromAssets copyFiles = new CopyFilesFromAssets(
						FragmentGeneral.this.getActivity(),
						Constants.configsPath);
				copyFiles.copyFileOrDir("libopenmw");

				try {
					Writer.write(
							Constants.configsPath + "/resources",
							Constants.configsPath + "/config/openmw/openmw.cfg",
							"resources");
					Writer.write(Constants.dataPath, Constants.configsPath
							+ "/config/openmw/openmw.cfg", "data");
					int pos = Settings.getInt(Constants.LANGUAGE, 0);

					Writer.write(
							FragmentSettings.data[pos],
							Constants.configsPath + "/config/openmw/openmw.cfg",
							"encoding");

					pos = Settings.getInt(Constants.MIPMAPPING, 0);

					Writer.write(FragmentSettings.dataMipmapping[pos],
							Constants.configsPath
									+ "/config/openmw/settings.cfg",
							"texture filtering");
					pos = Settings.getInt(Constants.SUBTITLES, -1);
					if (pos == 1)
						Writer.write("true", Constants.configsPath
								+ "/config/openmw/settings.cfg", "subtitles");
					else if (pos == 0)
						Writer.write("false", Constants.configsPath
								+ "/config/openmw/settings.cfg", "subtitles");

				} catch (Exception e) {
				}

				UI.sendEmptyMessage(0);
			}
		});
		th.start();
	}

	private void addTexWatchers() {
		configsText.addTextChangedListener(new TextListener(this.getActivity(),
				"/resources", "/config/openmw/openmw.cfg", "resources",
				Constants.CONFIGS_PATH, Constants.configsPath, Settings));

		dataText.addTextChangedListener(new TextListener(this.getActivity(),
				"", Constants.configsPath + "/config/openmw/openmw.cfg",
				"data", Constants.DATA_PATH, Constants.dataPath, Settings));

		commandLineText.addTextChangedListener(new TextListener(this
				.getActivity(), "", "", "", Constants.COMMAND_LINE,
				Constants.commandLineData, Settings));

	}

}

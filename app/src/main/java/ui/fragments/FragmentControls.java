package ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.libopenmw.openmw.R;

import constants.Constants;
import ui.screen.ScreenScaler;
import file.ConfigsFileStorageHelper;
import prefs.PreferencesHelper;

public class FragmentControls extends Fragment {

    private SharedPreferences Settings;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Settings = this.getActivity().getSharedPreferences(
                Constants.APP_PREFERENCES, Context.MODE_PRIVATE);
        View rootView = inflater.inflate(R.layout.settings, container, false);

        PreferencesHelper.getPrefValues(this.getActivity());
        changeTextViewSizes(rootView);
        int cameraStartPos = (int) (Settings.getFloat(Constants.CAMERA_MULTIPLISER, 2.0f)  );
        int touchStartPos = (int) (Settings.getFloat(Constants.TOUCH_SENSITIVITY, 0.01f) * 1000f);

        findViews(rootView, cameraStartPos, touchStartPos, Constants.TOUCH_SENSITIVITY, Constants.CAMERA_MULTIPLISER);

        return rootView;
    }


    private void changeTextViewSizes(View rootView) {
        ScreenScaler
                .changeTextSize(rootView.findViewById(R.id.cameraLabel), 3f);
        ScreenScaler
                .changeTextSize(rootView.findViewById(R.id.cameraValue), 3f);
        ScreenScaler
                .changeTextSize(rootView.findViewById(R.id.touchLabel), 3f);
        ScreenScaler
                .changeTextSize(rootView.findViewById(R.id.touchValue), 3f);


    }

    private void findViews(View rootView, int cameraStartPos, int touchStartPos, String touchPrefKey, String cameraPrefKey) {
        SeekBar touchSeekBar = (SeekBar) rootView.findViewById(R.id.touchBar);
        TextView touchProgress = (TextView) rootView.findViewById(R.id.touchValue);
        addSeekBarListener(touchSeekBar, touchStartPos, 100, 1000, touchProgress, touchPrefKey);

        SeekBar cameraSeekBar = (SeekBar) rootView.findViewById(R.id.cameraBar);
        TextView cameraProgress = (TextView) rootView.findViewById(R.id.cameraValue);
        addSeekBarListener(cameraSeekBar, cameraStartPos, 10, 1, cameraProgress, cameraPrefKey);

    }


    private void addSeekBarListener(SeekBar seekBar, int startProgress, int maxValue, final int step, final TextView seekBarValue, final String prefKey) {
        seekBar.setProgress(startProgress);
        seekBar.setMax(maxValue);
        final float[] finalProgress = {(float) startProgress / (float) step};
        seekBarValue.setText(String.valueOf(finalProgress[0]));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                finalProgress[0] = (float) progress / (float) step;
                seekBarValue.setText(String.valueOf(finalProgress[0]));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setPreferences(prefKey, finalProgress[0]);
                saveSeekBarProgress(String.valueOf(finalProgress[0]), prefKey);

            }
        });
    }

    private void saveSeekBarProgress(final String progress, final String key) {
        new Thread(new Runnable() {
            public void run() {

                try {
                    file.Writer.write(progress, ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH
                            + "/config/openmw/settings.cfg", key);

                } catch (Exception e)

                {
                }


            }
        }

        ).start();

    }


    private void setPreferences(String prefValue, float value) {
        Editor editor = Settings.edit();
        editor.putFloat(prefValue, value);
        editor.commit();

    }
}

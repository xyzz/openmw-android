package ui.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.widget.Toast;

import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.libopenmw.openmw.R;

import constants.Constants;
import file.ConfigsFileStorageHelper;
import ui.screen.ScreenResolutionHelper;

public class FragmentSettings extends PreferenceFragment implements OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        CheckBoxPreference subtitlescheckBoxPreference = (CheckBoxPreference) findPreference(Constants.SUBTITLES);
        subtitlescheckBoxPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                boolean showSubtitles = (boolean) newValue;
                saveSubtitlesSettings(showSubtitles);

                return true;
            }
        });


        ListPreference encodingList = (ListPreference) findPreference(Constants.LANGUAGE);

        encodingList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(final Preference preference, final Object newValue) {
                String encoding = newValue.toString();
                try {
                    file.Writer.write(
                            encoding,
                            ConfigsFileStorageHelper.OPENMW_CFG,
                            "encoding");

                } catch (Exception e) {

                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); ++i) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    Preference singlePref = preferenceGroup.getPreference(j);
                    updatePreference(singlePref, singlePref.getKey());
                }
            } else {
                updatePreference(preference, preference.getKey());
            }
        }
    }

    private void saveSubtitlesSettings(boolean showSubtitles) {

        try {
            file.Writer.write(String.valueOf(showSubtitles), ConfigsFileStorageHelper.SETTINGS_CFG, "subtitles");

        } catch (Exception e) {

            Toast toast = Toast.makeText(FragmentSettings.this
                            .getActivity().getApplicationContext(),
                    "configs files not found", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void saveMipMappingOptions(String mipmapping) {
        try {
            file.Writer.write(mipmapping, ConfigsFileStorageHelper.SETTINGS_CFG,
                    "texture filtering");

        } catch (Exception e) {
            Toast toast = Toast.makeText(FragmentSettings.this
                            .getActivity().getApplicationContext(),
                    "configs files not found",
                    Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private boolean isSensorAvailable() {
        PackageManager PM = this.getActivity().getPackageManager();
        return PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(findPreference(key), key);
    }

    private void updatePreference(Preference preference, String key) {
        if (preference == null)
            return;
        if (preference instanceof EditTextPreference) {
            EditTextPreference editTextPreference = (EditTextPreference) preference;
            editTextPreference.setSummary(editTextPreference.getText());
        }
    }

}

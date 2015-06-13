package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.libopenmw.openmw.R;

import constants.Constants;
import screen.ScreenScaler;
import ui.files.PreferencesHelper;
import ui.files.Writer;

public class FragmentGraphics extends Fragment {

    private int screenWidth = 0;
    private int screenHeight = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.graphics_layout, container, false);
        PreferencesHelper.getPrefValues(this.getActivity());
        getScreenWidthAndHeight();
        setupViews(rootView);
        return rootView;
    }


    private void getScreenWidthAndHeight() {
        Display display = FragmentGraphics.this.getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    private void setupViews(View rootView) {
        ScreenScaler.changeTextSize(rootView.findViewById(R.id.textView), 1.8f);
        ScreenScaler.changeTextSize(rootView.findViewById(R.id.X), 2.5f);
        RadioButton normalResolution = (RadioButton) rootView.findViewById(R.id.radioButtonNormalResolution);
        ScreenScaler.changeTextSize(normalResolution, 3f);

        RadioButton customlResolution = (RadioButton) rootView.findViewById(R.id.radioButtonCustomResolution);
        ScreenScaler.changeTextSize(customlResolution, 3f);

        RadioButton doublelResolution = (RadioButton) rootView.findViewById(R.id.radioButtonDoubleResolution);
        ScreenScaler.changeTextSize(doublelResolution, 3f);
        RadioButton halflResolution = (RadioButton) rootView.findViewById(R.id.radioButtonHalfResolution);
        ScreenScaler.changeTextSize(halflResolution, 3f);

        final EditText witdhEditText = (EditText) rootView.findViewById(R.id.width);
        final EditText heightEditText = (EditText) rootView.findViewById(R.id.height);
        witdhEditText.setText(PreferencesHelper.getPreferences(Constants.SCREEN_WIDTH, FragmentGraphics.this.getActivity(), "" + screenWidth));
        heightEditText.setText(PreferencesHelper.getPreferences(Constants.SCREEN_HEIGHT, FragmentGraphics.this.getActivity(), "" + screenHeight));

        witdhEditText.setEnabled(false);
        heightEditText.setEnabled(false);

        ScreenScaler.changeTextSize(witdhEditText, 2.5f);
        ScreenScaler.changeTextSize(heightEditText, 2.5f);

        RadioGroup radiogroup = (RadioGroup) rootView.findViewById(R.id.radioGroup);




        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonNormalResolution: {
                        witdhEditText.setEnabled(false);
                        heightEditText.setEnabled(false);
                        writeScreenDataToFile("" + screenWidth, "" + screenHeight);
                        PreferencesHelper.setPreferences(Constants.RADIO_BUTTON_MODE, "normalResolution", FragmentGraphics.this.getActivity());

                        break;
                    }
                    case R.id.radioButtonHalfResolution: {
                        witdhEditText.setEnabled(false);
                        heightEditText.setEnabled(false);
                        writeScreenDataToFile("" + screenWidth / 2, "" + screenHeight / 2);
                        PreferencesHelper.setPreferences(Constants.RADIO_BUTTON_MODE, "halfResolution", FragmentGraphics.this.getActivity());

                        break;
                    }

                    case R.id.radioButtonDoubleResolution: {
                        witdhEditText.setEnabled(false);
                        heightEditText.setEnabled(false);
                        writeScreenDataToFile("" + screenWidth * 2, "" + screenHeight * 2);
                        PreferencesHelper.setPreferences(Constants.RADIO_BUTTON_MODE, "doubleResolution", FragmentGraphics.this.getActivity());

                        break;
                    }
                    case R.id.radioButtonCustomResolution: {

                        writeScreenDataToFile(witdhEditText.getText().toString(), heightEditText.getText().toString());
                        witdhEditText.setEnabled(true);
                        heightEditText.setEnabled(true);
                        PreferencesHelper.setPreferences(Constants.RADIO_BUTTON_MODE, "customResolution", FragmentGraphics.this.getActivity());

                        break;
                    }


                    default:
                        break;
                }
            }
        });
        addTextWacthers(witdhEditText, heightEditText);

        String radioButtonMode = PreferencesHelper.getPreferences(Constants.RADIO_BUTTON_MODE, FragmentGraphics.this.getActivity(), "normalResolution");
        switch (radioButtonMode) {

            case "normalResolution":
                radiogroup.check(R.id.radioButtonNormalResolution);
                break;

            case "halfResolution":
                radiogroup.check(R.id.radioButtonHalfResolution);
                break;

            case "customResolution":
                radiogroup.check(R.id.radioButtonCustomResolution);
                break;

            case "doubleResolution":
                radiogroup.check(R.id.radioButtonDoubleResolution);
                break;

            default:
                break;

        }


    }


    private void addTextWacthers(EditText witdhEditText, EditText heightEditText) {
        witdhEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    Writer.write(s.toString(), Constants.configsPath
                            + "/config/openmw/settings.cfg", "resolution x");

                } catch (Exception e) {

                }

            }
        });


        heightEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                try {
                    Writer.write(s.toString(), Constants.configsPath
                            + "/config/openmw/settings.cfg", "resolution y");

                } catch (Exception e) {

                }

            }
        });

    }


    private void writeScreenDataToFile(String screenWidth, String screenHeight) {
        try {
            Writer.write(screenWidth, Constants.configsPath
                    + "/config/openmw/settings.cfg", "resolution x");
            Writer.write(screenHeight, Constants.configsPath
                    + "/config/openmw/settings.cfg", "resolution y");
        } catch (Exception e) {

        }

    }

}

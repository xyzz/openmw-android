package fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.libopenmw.openmw.R;

import constants.Constants;
import screen.ScreenScaler;
import ui.files.PreferencesHelper;
import ui.files.Writer;

/**
 * Created by sylar on 13.06.15.
 */
public class ScreenResolutionHelper {

    private Activity activity;
    private int screenWidth;
    private int screenHeight;

    public ScreenResolutionHelper(Activity activity) {
        this.activity = activity;
        getScreenWidthAndHeight();
    }

    private void getScreenWidthAndHeight() {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    public void setupViews(View rootView) {
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
        witdhEditText.setText(PreferencesHelper.getPreferences(Constants.SCREEN_WIDTH, activity, "" + screenWidth));
        heightEditText.setText(PreferencesHelper.getPreferences(Constants.SCREEN_HEIGHT, activity, "" + screenHeight));

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
                        PreferencesHelper.setPreferences(Constants.RADIO_BUTTON_MODE, "normalResolution", activity);

                        break;
                    }
                    case R.id.radioButtonHalfResolution: {
                        witdhEditText.setEnabled(false);
                        heightEditText.setEnabled(false);
                        writeScreenDataToFile("" + screenWidth / 2, "" + screenHeight / 2);
                        PreferencesHelper.setPreferences(Constants.RADIO_BUTTON_MODE, "halfResolution", activity);

                        break;
                    }

                    case R.id.radioButtonDoubleResolution: {
                        witdhEditText.setEnabled(false);
                        heightEditText.setEnabled(false);
                        writeScreenDataToFile("" + screenWidth * 2, "" + screenHeight * 2);
                        PreferencesHelper.setPreferences(Constants.RADIO_BUTTON_MODE, "doubleResolution", activity);

                        break;
                    }
                    case R.id.radioButtonCustomResolution: {

                        writeScreenDataToFile(witdhEditText.getText().toString(), heightEditText.getText().toString());
                        witdhEditText.setEnabled(true);
                        heightEditText.setEnabled(true);
                        PreferencesHelper.setPreferences(Constants.RADIO_BUTTON_MODE, "customResolution", activity);

                        break;
                    }


                    default:
                        break;
                }
            }
        });
        addTextWacthers(witdhEditText, heightEditText);

        String radioButtonMode = PreferencesHelper.getPreferences(Constants.RADIO_BUTTON_MODE, activity, "normalResolution");
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

    public void writeScreenResolution() {
        String radioButtonMode = PreferencesHelper.getPreferences(Constants.RADIO_BUTTON_MODE, activity, "normalResolution");
        switch (radioButtonMode) {

            case "normalResolution":
                writeScreenDataToFile("" + screenWidth, "" + screenHeight);
                break;

            case "halfResolution":
                writeScreenDataToFile("" + screenWidth / 2, "" + screenHeight / 2);
                break;

            case "customResolution":
                writeScreenDataToFile(PreferencesHelper.getPreferences(Constants.SCREEN_WIDTH, activity, "" + screenWidth),
                        PreferencesHelper.getPreferences(Constants.SCREEN_HEIGHT, activity, "" + screenHeight));
                break;

            case "doubleResolution":
                writeScreenDataToFile("" + screenWidth * 2, "" + screenHeight * 2);
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

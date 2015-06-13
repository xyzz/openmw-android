package fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

/**
 * Created by sylar on 13.06.15.
 */
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
        witdhEditText.setText("" + screenWidth);
        heightEditText.setText("" + screenHeight);
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

                        writeScreenDataToFile(screenWidth, screenHeight);

                        break;
                    }
                    case R.id.radioButtonHalfResolution: {
                        writeScreenDataToFile(screenWidth / 2, screenHeight / 2);

                        break;
                    }

                    case R.id.radioButtonDoubleResolution: {
                        writeScreenDataToFile(screenWidth * 2, screenHeight * 2);

                        break;
                    }
                    case R.id.radioButtonCustomResolution: {

                        witdhEditText.setEnabled(true);
                        heightEditText.setEnabled(true);
                        break;
                    }


                    default:
                        break;
                }
            }
        });


    }


    private void writeScreenDataToFile(int screenWidth, int screenHeight) {
        try {
            Writer.write("" + screenWidth, Constants.configsPath
                    + "/config/openmw/settings.cfg", "resolution x");
            Writer.write("" + screenHeight, Constants.configsPath
                    + "/config/openmw/settings.cfg", "resolution y");
        } catch (Exception e) {

        }

    }

}

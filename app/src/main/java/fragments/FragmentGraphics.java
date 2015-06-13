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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.graphics_layout, container, false);
        PreferencesHelper.getPrefValues(this.getActivity());
        ScreenResolutionHelper screenHelper= new ScreenResolutionHelper(FragmentGraphics.this.getActivity());
        screenHelper.setupViews(rootView);
        return rootView;
    }



}

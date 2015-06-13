package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.libopenmw.openmw.R;

import ui.files.PreferencesHelper;

/**
 * Created by sylar on 13.06.15.
 */
public class FragmentGraphics extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.graphics_layout, container, false);

        PreferencesHelper.getPrefValues(this.getActivity());

        return rootView;
    }
}

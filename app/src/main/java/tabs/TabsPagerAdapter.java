package tabs;

import fragments.FragmentControls;
import fragments.FragmentGeneral;
import fragments.FragmentGraphics;
import fragments.FragmentPlugins;
import fragments.FragmentSettings;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                return new FragmentGeneral();
            case 1:
                return new FragmentControls();
            case 2:
                return new FragmentSettings();

            case 3:
                return new FragmentPlugins();
            case 4:
                return new FragmentGraphics();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }

}
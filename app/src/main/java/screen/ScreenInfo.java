package screen;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by sandstranger on 04.02.16.
 */
public class ScreenInfo {

    public float screenWidth = 0;
    public float screenHeight = 0;
    private DisplayMetrics dm;

    public ScreenInfo(Context context) {
        getScreenSize(context);
    }

    private void getScreenSize(Context context) {
        dm = context.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    public double diagonalSize() {
        double size = 0;
        try {
            size =  Math.sqrt(Math.pow(screenWidth / dm.xdpi, 2) +
                    Math.pow(screenHeight / dm.ydpi, 2));
        } catch (Exception e) {

        }
        return size;
    }

}


package fragments;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

import constants.Constants;
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
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();

        screenWidth = (int)(dm.widthPixels / dm.xdpi);

        screenHeight =(int) (dm.heightPixels / dm.ydpi);
    }


    public void writeScreenResolution(String currentResolutionMode) {
        switch (currentResolutionMode) {

            case "normalResolution":
                writeScreenDataToFile("" + screenWidth, "" + screenHeight);
                break;

            case "halfResolution":
                writeScreenDataToFile("" + screenWidth / 2, "" + screenHeight / 2);
                break;

            case "doubleResolution":
                writeScreenDataToFile("" + screenWidth * 2, "" + screenHeight * 2);
                break;

            default:
                break;

        }

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

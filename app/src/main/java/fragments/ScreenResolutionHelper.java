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

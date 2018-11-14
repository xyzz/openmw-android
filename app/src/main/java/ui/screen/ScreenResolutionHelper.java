package ui.screen;

import android.app.Activity;
import android.view.View;

import java.io.IOException;

import ui.screen.ScreenInfo;
import file.ConfigsFileStorageHelper;

/**
 * Created by sylar on 13.06.15.
 */
public class ScreenResolutionHelper {

    private int screenWidth;
    private int screenHeight;

    public ScreenResolutionHelper(Activity activity) {
        ScreenInfo screenInfo = new ScreenInfo(activity);
        screenWidth = Math.round(screenInfo.screenWidth);
        screenHeight = Math.round(screenInfo.screenHeight);
    }

    public void writeScreenResolution() throws IOException {
        file.Writer.write(String.valueOf(screenWidth), ConfigsFileStorageHelper.SETTINGS_CFG, "resolution x");
        file.Writer.write(String.valueOf(screenHeight), ConfigsFileStorageHelper.SETTINGS_CFG, "resolution y");
    }

}

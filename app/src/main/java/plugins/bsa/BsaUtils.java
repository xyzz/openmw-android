package plugins.bsa;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import constants.Constants;
import file.utils.FileUtils;
import plugins.PluginInfo;
import prefs.PreferencesHelper;

public class BsaUtils {
    private static final String SAVE_ALL_BSA_KEY = "save_all_bsa";
    private List<File> bsaList = new ArrayList<File>();

    public BsaUtils() {
        try {
            bsaList = Arrays.asList(new File(Constants.APPLICATION_DATA_STORAGE_PATH).
                    listFiles((d, name) -> (name.endsWith(".BSA") || name.endsWith(".bsa"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBsaFileName(PluginInfo pluginInfo) {
        String pluginFileName = FileUtils.getFileName(pluginInfo.name, false);
        if (bsaList != null) {
            for (File file : bsaList) {
                if (file.isFile() && file.getName().contains(pluginFileName)) {
                    return file.getName();
                }
            }
        }
        return "";
    }

    public List<File> getBsaList() {
        return bsaList != null ? bsaList : new ArrayList<>();
    }

    public static boolean getSaveAllBsaFilesValue(Activity activity) {
        return PreferencesHelper.getPreferences(SAVE_ALL_BSA_KEY, activity);
    }

    public static void setSaveAllBsaFilesValue(Activity activity, boolean value) {
        PreferencesHelper.setPreferences(SAVE_ALL_BSA_KEY, activity, value);
    }

}

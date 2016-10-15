package plugins.bsa;

import android.app.Activity;
import android.preference.Preference;

import java.io.File;
import java.util.List;

import file.utils.FileUtils;
import plugins.PluginInfo;
import plugins.PluginsUtils;
import ui.files.ConfigsFileStorageHelper;
import ui.files.PreferencesHelper;
import ui.fragments.FragmentPlugins;

/**
 * Created by sandstranger on 14.10.16.
 */

public class BsaWriter {
    private static final String SAVE_ALL_BSA_KEY = "save_all_bsa";

    public static boolean getSaveAllBsaFilesValue(Activity activity) {
        return PreferencesHelper.getPreferences(SAVE_ALL_BSA_KEY, activity);
    }

    public static void setSaveAllBsaFilesValue(Activity activity, boolean value) {
        PreferencesHelper.setPreferences(SAVE_ALL_BSA_KEY, activity, value);
    }

    public static void saveAllBsaFiles(Activity activity, boolean needSaveAllBsaFiles) {
        try {
            setSaveAllBsaFilesValue(activity, needSaveAllBsaFiles);
            if (FragmentPlugins.getInstance() != null) {
                PluginsUtils.savePlugins(FragmentPlugins.getInstance().getPluginsStorage().getPluginsList(), activity);
                if (needSaveAllBsaFiles) {
                    BsaUtils bsaUtils = new BsaUtils();
                    List<File> list = bsaUtils.getBsaList();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (File f : list) {
                        stringBuilder.append("fallback-archive= " + f.getName() + "\n");
                    }
                    String pathToFile = ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH
                            + "/openmw/openmw.cfg";
                    FileUtils.saveDataToFile(stringBuilder.toString(), pathToFile, true);
                }
            }
        }
        catch (Exception e){

        }
    }
}

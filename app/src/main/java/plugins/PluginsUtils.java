package plugins;

import java.io.File;
import java.io.IOException;
import java.util.List;

import constants.Constants;
import ui.files.ConfigsFileStorageHelper;
import utils.Utils;

/**
 * Created by sandstranger on 07.09.2016.
 */
public class PluginsUtils {
    public static String getBsaFileName(PluginInfo pluginInfo) {
        File dir = new File(Constants.APPLICATION_DATA_STORAGE_PATH);
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                String bsaExtension = "";
                if (file.getName().endsWith(".bsa")) {
                    bsaExtension = ".bsa";
                } else if (file.getName().endsWith(".BSA")) {
                    bsaExtension = ".BSA";
                }
                if (!bsaExtension.isEmpty() && pluginInfo.name.contains(file.getName().replace(bsaExtension, ""))) {
                    return file.getName();
                }
            }
        }
        return "";
    }

    public static void savePlugins(List<PluginInfo> pluginsList) {
        try {
            String pathToFile = ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH
                    + "/openmw/openmw.cfg";
            StringBuilder stringBuilder = new StringBuilder();
            for (PluginInfo pluginInfo : pluginsList) {
                if (pluginInfo.enabled == 1) {
                    stringBuilder.append("content= " + pluginInfo.name + "\n");
                    String bsaFileNameName = PluginsUtils.getBsaFileName(pluginInfo);
                    if (!bsaFileNameName.isEmpty()) {
                        stringBuilder.append("fallback-archive= "
                                + bsaFileNameName + "\n");
                    }
                }
            }
            Utils.saveDataToFile(stringBuilder.toString(), pathToFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

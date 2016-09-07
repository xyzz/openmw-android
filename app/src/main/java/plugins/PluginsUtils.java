package plugins;

import java.io.File;

import constants.Constants;

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


}

package plugins;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import constants.Constants;
import ui.files.ConfigsFileStorageHelper;
import utils.Utils;

/**
 * Created by sandstranger on 07.09.2016.
 */
public class PluginsUtils {

    public static String getBsaFileName(PluginInfo pluginInfo) {
        String pluginFileName = pluginInfo.name.replace(".esm", "").replace(".ESM", "").replace(".ESP", "").replace(".esp", "");
        List<File> fileList = Arrays.asList(new File(Constants.APPLICATION_DATA_STORAGE_PATH).
                listFiles((d, name) -> (d.isFile() && (name.endsWith(".BSA") || name.endsWith("bsa")))));
        Optional<File> optional = fileList.stream().filter(item -> item.getName().contains(pluginFileName)).findFirst();
        if (optional != null && optional.isPresent()) {
            return optional.get().getName();
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

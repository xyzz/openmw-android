package plugins;

import java.util.List;

import file.utils.FileUtils;
import plugins.bsa.BsaUtils;
import ui.files.ConfigsFileStorageHelper;

/**
 * Created by sandstranger on 07.09.2016.
 */
public class PluginsUtils {

    public static void savePlugins(List<PluginInfo> pluginsList) {
        try {
            BsaUtils bsaUtils = new BsaUtils();
            String pathToFile = ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH
                    + "/openmw/openmw.cfg";
            StringBuilder stringBuilder = new StringBuilder();
            for (PluginInfo pluginInfo : pluginsList) {
                if (pluginInfo.enabled) {
                    stringBuilder.append("content= " + pluginInfo.name + "\n");
                    String bsaFileNameName = bsaUtils.getBsaFileName(pluginInfo);
                    if (!bsaFileNameName.isEmpty()) {
                        stringBuilder.append("fallback-archive= "
                                + bsaFileNameName + "\n");
                    }
                }
            }
            FileUtils.saveDataToFile(stringBuilder.toString(), pathToFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

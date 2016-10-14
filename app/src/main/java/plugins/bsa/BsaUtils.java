package plugins.bsa;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import constants.Constants;
import plugins.PluginInfo;

public class BsaUtils {
    private List<File> bsaList = new ArrayList<File>();

    public BsaUtils() {
        bsaList = Arrays.asList(new File(Constants.APPLICATION_DATA_STORAGE_PATH).
                listFiles((d, name) -> (name.endsWith(".BSA") || name.endsWith("bsa"))));
    }

    public String getBsaFileName(PluginInfo pluginInfo) {
        String pluginFileName = pluginInfo.name.replace(".esm", "").replace(".ESM", "").replace(".ESP", "").replace(".esp", "").replace(".omwaddon","").replace("omwgame","");
        for (File file : bsaList) {
            if (file.isFile() && file.getName().contains(pluginFileName)) {
                return file.getName();
            }
        }
        return "";
    }
}

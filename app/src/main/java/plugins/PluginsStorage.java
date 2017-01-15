package plugins;

import android.app.Activity;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import constants.Constants;
import file.utils.FileUtils;
import parser.json.JsonReader;
import file.ConfigsFileStorageHelper;
import plugins.bsa.BsaUtils;
import utils.Utils;

/**
 * Created by sandstranger on 07.09.2016.
 */
public class PluginsStorage {
    private final String dataPath = Constants.APPLICATION_DATA_STORAGE_PATH;
    private List<PluginInfo> pluginsList = new ArrayList<PluginInfo>();
    private File dataDir = new File(dataPath);
    private final String JSON_FILE_LOCATION = ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH + "/files.json";
    private final String CFG_FILE_LOCATION =  ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH + "/openmw/openmw.cfg";
    private Activity activity;

    public PluginsStorage(Activity activity) {
        this.activity = activity;
        loadPlugins(JSON_FILE_LOCATION);
    }

    public List<PluginInfo> getPluginsList() {
        return pluginsList;
    }

    public void loadPlugins(String path) {

        try {
            pluginsList = JsonReader.loadFile(path);
            removeDeletedFiles();
            addNewFiles();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.deleteFile(JSON_FILE_LOCATION);
            loadPlugins(JSON_FILE_LOCATION);
        }
    }

    private boolean isListContainsFile(File f) {
        for (PluginInfo plugin : pluginsList) {
            if (f.isFile() && !plugin.name.isEmpty() && f.getName().endsWith(plugin.name)) {
                return true;
            }
        }
        return false;
    }

    public void replacePlugins(int from, int to) {
        PluginInfo item = pluginsList.get(from);
        pluginsList.remove(from);
        pluginsList.add(to, item);
    }

    private void addNewFiles() throws JSONException, IOException {
        File[] files = dataDir.listFiles((d, name) -> name.endsWith(".ESM") || name.endsWith(".ESP") || name.endsWith(".esp") || name.endsWith(".esm") ||
                name.endsWith(".omwgame") || name.endsWith(".omwaddon"));
        boolean isFileAdded = false;
        for (File f : files) {
            if (!isListContainsFile(f)) {
                isFileAdded = true;
                PluginInfo pluginData = new PluginInfo();
                pluginData.name = f.getName();
                pluginData.nameBsa = f.getName().split("\\.")[0] + ".bsa";
                pluginData.isPluginEsp = f.getName().endsWith(".ESP") || f.getName().endsWith(".esp");
                pluginData.pluginExtension = FileUtils.getFileName(f.getName(), true);
                pluginsList.add(pluginData);
            }
        }
        if (isFileAdded) {
            sortPlugins();
        }
    }

    private void sortPlugins() {
        Collections.sort(pluginsList, (p1, p2) -> Boolean.compare(p1.isPluginEsp, p2.isPluginEsp));
    }

    private void removeDeletedFiles() {
        Iterator<PluginInfo> iterator = pluginsList.iterator();
        while (iterator.hasNext()) {
            PluginInfo pluginInfo = iterator.next();
            File file = new File(dataPath + "/" + pluginInfo.name);
            if (!file.exists()) {
                pluginsList.remove(pluginInfo);
            }
        }
    }

    public void updatePluginsStatus(boolean needEnableMods) {
        for (int i = 0; i < pluginsList.size(); i++) {
            pluginsList.get(i).enabled = needEnableMods;
        }
    }

    public void updatePluginStatus(int position, boolean status) {
        pluginsList.get(position).enabled = status;
    }

    public void saveJson(final String path) {
        String finalPath = path.isEmpty() ? JSON_FILE_LOCATION : path;
        try {
            JsonReader.saveFile(pluginsList, finalPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePlugins() {
        try {
            boolean needRegisterBsaFiles = BsaUtils.getSaveAllBsaFilesValue(activity);
            BsaUtils bsaUtils = new BsaUtils();
            StringBuilder stringBuilder = new StringBuilder();
            for (PluginInfo pluginInfo : pluginsList) {
                if (pluginInfo.enabled) {
                    stringBuilder.append("content= " + pluginInfo.name + "\n");
                    if (!needRegisterBsaFiles) {
                        String bsaFileNameName = bsaUtils.getBsaFileName(pluginInfo);
                        if (!bsaFileNameName.isEmpty()) {
                            stringBuilder.append("fallback-archive= "
                                    + bsaFileNameName + "\n");
                        }
                    }
                }
            }
            if (needRegisterBsaFiles) {
                List<File> list = bsaUtils.getBsaList();
                for (File f : list) {
                    stringBuilder.append("fallback-archive= " + f.getName() + "\n");
                }
            }
            FileUtils.saveDataToFile(stringBuilder.toString(), CFG_FILE_LOCATION, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


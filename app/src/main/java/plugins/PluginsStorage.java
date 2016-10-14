package plugins;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import constants.Constants;
import parser.json.JsonReader;
import ui.files.ConfigsFileStorageHelper;

/**
 * Created by sandstranger on 07.09.2016.
 */
public class PluginsStorage {
    private final String dataPath = Constants.APPLICATION_DATA_STORAGE_PATH;
    private List<PluginInfo> pluginsList = new ArrayList<PluginInfo>();
    private File dataDir = new File(dataPath);
    private final static String JSON_FILE_LOCATION = ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH + "/files.json";
    private Context context;

    public PluginsStorage(Context context) {
        this.context = context;
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
            Toast.makeText(
                    context,
                    "data files not found", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isListContainsFile(File f) {
        for (PluginInfo plugin : pluginsList ) {
            if (f.isFile() && !plugin.name.isEmpty() && f.getName().endsWith(plugin.name)){
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

    private int getLastEsmPosition() {
        int lastEsmPos = 0;
        for (int i = 0; i < pluginsList.size(); i++) {
            if (pluginsList.get(i).name.endsWith(".esm")
                    || pluginsList.get(i).name.endsWith(".ESM"))
                lastEsmPos = i;
            else
                break;
        }
        return lastEsmPos;
    }

    private void addNewFiles() throws JSONException, IOException {
        int lastEsmPos = getLastEsmPosition();
        for (PluginInfo plugin:pluginsList){
            Log.d("PLUGIN",plugin.name);
        }

        File[] files = dataDir.listFiles((d,name) -> name.endsWith(".ESM") || name.endsWith(".ESP") || name.endsWith(".esp") || name.endsWith(".esm"));
        for (File f : files) {
            if (!isListContainsFile(f)) {
                PluginInfo pluginData = new PluginInfo();
                pluginData.name = f.getName();
                pluginData.nameBsa = f.getName().split("\\.")[0] + ".bsa";
                if (f.getName().endsWith(".esm")
                        || f.getName().endsWith(".ESM")) {
                    pluginsList.add(lastEsmPos, pluginData);
                    lastEsmPos++;
                } else if (f.getName().endsWith(".esp")
                        || f.getName().endsWith(".ESP")
                        || f.getName().endsWith(".omwgame")
                        || f.getName().endsWith(".omwaddon")) {
                    pluginsList.add(pluginData);
                }
            }
        }
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

    public void updatePluginStatus(int position ,boolean status){
        pluginsList.get(position).enabled = status;
    }

    public void savePluginsData(final String path) {
           String finalPath = path.isEmpty() ? JSON_FILE_LOCATION : path;
        try {
            JsonReader.saveFile(pluginsList, finalPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


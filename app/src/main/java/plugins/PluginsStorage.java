package plugins;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import constants.Constants;
import fragments.FragmentPlugins;
import json.JsonReader;
import ui.files.ConfigsFileStorageHelper;
import utils.Utils;

/**
 * Created by sandstranger on 07.09.2016.
 */
public class PluginsStorage {
    private List<PluginInfo> pluginsList = new ArrayList<PluginInfo>();
    private File dataDir = new File(Constants.APPLICATION_DATA_STORAGE_PATH);
    private final static String JSON_FILE_LOCATION = ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH + "/files.json";
    private Context context;

    public PluginsStorage(Context context) {
        this.context = context;
        loadPlugins(JSON_FILE_LOCATION);
        PluginsUtils.savePlugins(pluginsList);
    }

    public List<PluginInfo> getPluginsList() {
        return pluginsList;
    }
    
    private void loadPlugins(String path) {
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
        for (PluginInfo data : pluginsList) {
            if (f.isFile() && f.getName().endsWith(data.name)) {
                return true;
            }
        }
        return false;
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
        for (File f : dataDir.listFiles()) {
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
            boolean isFileExists = false;
            for (File f : dataDir.listFiles()) {
                if (f.isFile() && f.getName().endsWith(iterator.next().name)) {
                    isFileExists = true;
                    break;
                }
            }
            if (!isFileExists) {
                pluginsList.remove(iterator.next());
            }
        }
    }
}

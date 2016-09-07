package json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {
    private static final String NAME_PLUGIN_KEY = "name";
    private static final String NAME_BSA_KEY = "nameBsa";
    private static final String PLUGIN_ENABLED_KEY = "enabled";

    public static void saveFile(List<PluginInfo> loadedFile, String path)
            throws JSONException, IOException {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < loadedFile.size(); i++) {
            JSONObject c = new JSONObject();
            c.put(NAME_PLUGIN_KEY, loadedFile.get(i).name);
            c.put(NAME_BSA_KEY, loadedFile.get(i).nameBsa);
            c.put(PLUGIN_ENABLED_KEY, loadedFile.get(i).enabled);
            jsonArray.put(c);
        }
        FileWriter jsonFileWriter = new FileWriter(path);
        jsonFileWriter.write(jsonArray.toString());
        jsonFileWriter.flush();
        jsonFileWriter.close();
    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    private static String getJsonString(String jsonFilePath) throws IOException {
        String input = "";
        File inputfile = new File(jsonFilePath);
        if (!inputfile.exists()) {
            inputfile.createNewFile();
        }
        FileInputStream fin = new FileInputStream(inputfile);
        input = convertStreamToString(fin);
        fin.close();
        return input;
    }

    public static List<PluginInfo> loadFile(String jsonFilePath) {
        List<PluginInfo> ret = new ArrayList<PluginInfo>();
        try {
            JSONArray jsonArray = new JSONArray(getJsonString(jsonFilePath));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                PluginInfo ti = new PluginInfo();
                ti.name = obj.getString(NAME_PLUGIN_KEY);
                ti.nameBsa = obj.getString(NAME_BSA_KEY);
                ti.enabled = obj.getLong(PLUGIN_ENABLED_KEY);
                ret.add(ti);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static class PluginInfo {
        public String name = "";
        public String nameBsa = "";
        public long enabled = 0;
    }
}

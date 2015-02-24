package ui.files;

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

import ui.activity.MainActivity;

public class ParseJson {

	public static final String jsonFilePath = MainActivity.configsPath
			+ "/files.json";

	public static void savetofile(FilesData ti) throws IOException {
		List<FilesData> loadedFile = loadFile();
		loadedFile.add(ti);

		try {
			saveFile(loadedFile);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void saveFile(List<FilesData> loadedFile)
			throws JSONException, IOException {

		String file = "";

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < loadedFile.size(); i++) {

			JSONObject c;
			c = new JSONObject();

			c.put("name", loadedFile.get(i).name);
			c.put("nameBsa", loadedFile.get(i).nameBsa);
			c.put("enabled", loadedFile.get(i).enabled);

			jsonArray.put(c);

		}
		JSONObject array = new JSONObject();
		array.put("data_array", jsonArray);
		file = array.toString();

		FileWriter jsonFileWriter = new FileWriter(jsonFilePath);

		jsonFileWriter.write(file);

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

	public static List<FilesData> loadFile() throws IOException {
		List<FilesData> ret = new ArrayList<ParseJson.FilesData>();

		try {

			String input = "";
			File inputfile = new File(jsonFilePath);
			inputfile.createNewFile();
			FileInputStream fin = new FileInputStream(inputfile);
			input = convertStreamToString(fin);

			if (input != "") {
				JSONObject jsonObject = new JSONObject(input);
				JSONArray jsonArray = jsonObject.getJSONArray("data_array");
				List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
				for (int i = 0; i < jsonArray.length(); i++) {
					jsonObjects.add((JSONObject) jsonArray.get(i));
				}
				for (JSONObject obj : jsonObjects) {
					FilesData ti = new FilesData();
					ti.name = obj.getString("name");
					ti.nameBsa = obj.getString("nameBsa");
					ti.enabled = obj.getLong("enabled");
					ret.add(ti);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;

	}

	public static class FilesData {
		public FilesData() {
			this.name = "";
			this.nameBsa = "";
			this.enabled = 0;

		}

		public String name;
		public String nameBsa;
		public long enabled;

	}

}

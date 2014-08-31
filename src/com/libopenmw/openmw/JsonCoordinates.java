package com.libopenmw.openmw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonCoordinates {

	private static final String jsonFilePath = "/sdcard/morrowind/coord.json";
	public static int pos = -1;

	public static CoordData data;

	public static CoordData dataUpdate;

	public static void savetofile(CoordData ti) throws IOException {
		List<CoordData> loadedFile = loadFile();
		loadedFile.add(ti);

		try {
			saveFile(loadedFile);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void saveFile(List<CoordData> loadedFile)
			throws JSONException, IOException {

		String file = "";

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < loadedFile.size(); i++) {

			JSONObject c;
			c = new JSONObject();

			c.put("name", loadedFile.get(i).name);
			c.put("x", loadedFile.get(i).x);
			c.put("y", loadedFile.get(i).y);
			c.put("y", loadedFile.get(i).width);
			c.put("height", loadedFile.get(i).height);

			jsonArray.put(c);
		}
		JSONObject array = new JSONObject();
		array.put("data_array", jsonArray);
		file = array.toString();

		FileWriter jsonFileWriter = new FileWriter(jsonFilePath);

		jsonFileWriter.write(file);

		jsonFileWriter.flush();

	}

	public static void updatetofile(CoordData ti) throws IOException {
		List<CoordData> loadedFile = loadFile();
		dataUpdate = ti;

		try {
			updateFile(loadedFile);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void updateFile(List<CoordData> loadedFile)
			throws JSONException, IOException {

		String file = "";

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < loadedFile.size(); i++) {

			JSONObject c;
			c = new JSONObject();

			if (i == pos) {

				c.put("name", loadedFile.get(i).name);
				c.put("x", dataUpdate.x);
				c.put("y", dataUpdate.y);
				c.put("width", dataUpdate.width);
				c.put("height", dataUpdate.height);

			} else {
				c.put("name", loadedFile.get(i).name);
				c.put("x", loadedFile.get(i).x);
				c.put("y", loadedFile.get(i).y);
				c.put("y", loadedFile.get(i).width);
				c.put("height", loadedFile.get(i).height);

			}

			jsonArray.put(c);
		}
		JSONObject array = new JSONObject();
		array.put("data_array", jsonArray);
		file = array.toString();

		FileWriter jsonFileWriter = new FileWriter(jsonFilePath);

		jsonFileWriter.write(file);

		jsonFileWriter.flush();

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

	public static List<CoordData> loadFile() throws IOException {
		List<CoordData> ret = new ArrayList<JsonCoordinates.CoordData>();

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
					CoordData ti = new CoordData();
					ti.name = obj.getString("name");
					ti.x = obj.getLong("x");
					ti.y = obj.getLong("y");
					ti.width = obj.getLong("width");
					ti.height = obj.getLong("height");
					ret.add(ti);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return ret;

	}

	public static class CoordData {
		public CoordData() {
			this.name = "";
			this.y = 0;
			this.x = 0;
			this.width = 0;
			this.height = 0;

		}

		public String name;
		public long x;
		public long y;
		public long width;
		public long height;

	}

}

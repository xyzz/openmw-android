package com.libopenmw.openmw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.libopenmw.openmw.ParseJson.FilesData;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PluginsView extends Activity {

	public List<FilesData> Plugins;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

		try {
			Plugins = ParseJson.loadFile();
			if (Plugins == null)
				Plugins = new ArrayList<ParseJson.FilesData>();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File yourDir = new File(MainActivity.dataPath);

		try {
			checkFilesDeleted(yourDir);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			addNewFiles(yourDir);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ListView listView = (ListView) findViewById(R.id.listView1);

		listView.setAdapter(new Adapter());

	}

	public void savePlugins(View v) throws IOException {
		List<FilesData> plugins = null;

		try {
			plugins = ParseJson.loadFile();

			FileWriter writer = new FileWriter(MainActivity.configsPath
					+ "/openmw/openmw.cfg");

			int i = 0;
			while (i < plugins.size()) {

				if (plugins.get(i).enabled == 1) {
					writer.write("content= " + plugins.get(i).name + "\n");
					writer.write("fallback-archive= " + plugins.get(i).nameBsa
							+ "\n");

					writer.flush();
				}
				i++;

			}
			writer.close();
			Toast toast = Toast.makeText(getApplicationContext(),
					"Saving done", Toast.LENGTH_LONG);
			toast.show();

		} catch (Exception e) {
			Toast toast = Toast.makeText(getApplicationContext(),
					"config file openmw.cfg not found", Toast.LENGTH_LONG);
			toast.show();
			e.printStackTrace();
		}
	}

	private void checkFilesDeleted(File yourDir) throws JSONException,
			IOException {
		int index = 0;
		for (FilesData data : Plugins) {
			boolean fileDeleted = true;

			for (File f : yourDir.listFiles()) {

				if (f.isFile() && f.getName().contains(data.name)) {

					fileDeleted = false;
					break;

				} else
					fileDeleted = true;

			}

			if (fileDeleted) {
				Plugins.remove(index);
			}
			index++;
		}
		if (Plugins.size() < index)
			ParseJson.saveFile(Plugins);

	}

	private void addNewFiles(File yourDir) throws JSONException, IOException {
		int lastEsmPos = 0;

		for (int i = 0; i < Plugins.size(); i++) {
			if (Plugins.get(i).name.endsWith("esm"))
				lastEsmPos = i;
			else
				break;
		}

		for (File f : yourDir.listFiles()) {

			boolean newPlugin = true;
			for (FilesData data : Plugins) {
				if (f.isFile() && f.getName().contains(data.name)) {

					newPlugin = false;
					break;

				} else
					newPlugin = true;

			}
			if (newPlugin) {
				FilesData pluginData = new FilesData();

				pluginData.name = f.getName();
				pluginData.nameBsa = f.getName().split("\\.")[0] + ".bsa";
				if (f.getName().endsWith("esm")) {
					Plugins.add(lastEsmPos, pluginData);
					lastEsmPos++;
				} else if (f.getName().endsWith("esp")) {
					Plugins.add(pluginData);
				}

			}

		}

		// sort Plugins
		for (int i = 0; i < Plugins.size(); i++) {
			if (Plugins.get(i).name.contains("Morrowind.esm") && i != 0) {
				changeLinesInList(0, i);
				break;
			} else

			if (Plugins.get(i).name.contains("Bloodmoon.esm") && i != 1)
				changeLinesInList(1, i);
			else if (Plugins.get(i).name.contains("Tribunal.esm") && i != 2)
				changeLinesInList(2, i);

		}

		ParseJson.saveFile(Plugins);
	}

	private void changeLinesInList(int line1, int line2) {
		FilesData value1;
		value1 = Plugins.get(line1);
		FilesData value2;
		value2 = Plugins.get(line2);

		Log.d("DEBUG", value1.name);
		Log.d("DEBUG", value2.name);

		Plugins.remove(line1);
		Plugins.add(line1, value2);
		Plugins.remove(line2);
		Plugins.add(line2, value1);

	}

	public class Adapter implements ListAdapter

	{

		public View rowView;

		@Override
		public boolean isEmpty() {

			// TODO Auto-generated method stub

			return false;

		}

		@Override
		public boolean hasStableIds() {

			// TODO Auto-generated method stub

			return false;

		}

		@Override
		public int getViewTypeCount() {

			// TODO Auto-generated method stub

			return 1;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) PluginsView.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			rowView = inflater.inflate(R.layout.rowlistview, parent, false);

			TextView data = (TextView) rowView.findViewById(R.id.textView1);
			TextView enabled = (TextView) rowView
					.findViewById(R.id.textViewenabled);

			final CheckBox Box = (CheckBox) rowView
					.findViewById(R.id.checkBoxenable);
			enabled.setText(String.valueOf(Plugins.get(position).enabled));

			if (Plugins.get(position).enabled == 1)
				Box.setChecked(true);

			Box.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (Box.isChecked()) {

						Plugins.get(position).enabled = 1;
						try {
							ParseJson.saveFile(Plugins);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {

						Plugins.get(position).enabled = 0;
						try {
							ParseJson.saveFile(Plugins);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

			});

			data.setText(Plugins.get(position).name);
			return rowView;

		}

		@Override
		public int getItemViewType(int position) {

			// TODO Auto-generated method stub

			return 0;

		}

		@Override
		public long getItemId(int position) {

			// TODO Auto-generated method stub

			return 0;

		}

		@Override
		public Object getItem(int position) {

			// TODO Auto-generated method stub

			return null;

		}

		@Override
		public int getCount() {

			return Plugins.size();

		}

		@Override
		public boolean isEnabled(int position) {

			// TODO Auto-generated method stub

			return false;

		}

		@Override
		public boolean areAllItemsEnabled() {

			// TODO Auto-generated method stub

			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub

		}

	}

}

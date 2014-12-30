package com.libopenmw.openmw;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.libopenmw.openmw.FileRW.FilesData;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Environment;
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

	public Context context;
	public static List<FilesData> loadedFile;
	public static List<FilesData> loadedFileCheck;
	public boolean check = false;
	String name;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);
		try {

			loadedFileCheck = null;
			loadedFileCheck = FileRW.loadFile();
			// File sdCardRoot = Environment.getExternalStorageDirectory();
			File yourDir = new File(MainActivity.dataPath);

			for (int i = 0; i < loadedFileCheck.size(); i++) {
				boolean deleteFlag = true;

				for (File f : yourDir.listFiles()) {

					if (f.isFile()
							&& f.getName()
									.contains(loadedFileCheck.get(i).name)) {

						deleteFlag = false;

					} else if (deleteFlag != false)
						deleteFlag = true;

				}

				if (deleteFlag) {
					FileRW.DeleteF(loadedFileCheck, i);

				}
			}

			for (File f : yourDir.listFiles()) {

				if (f.isFile()) {

					check = false;
					FilesData data = new FilesData();
					String[] esp = f.getName().split("\\.");
					if (f.getName().equals("Morrowind.esm")) {

						try {
							int i = 0;
							while (i < loadedFileCheck.size() && check == false) {
								if (loadedFileCheck.get(i).name.equals(f
										.getName()))
									check = true;
								else
									check = false;
								i++;
							}
							if (check == false) {
								data.name = f.getName();
								data.nameBsa = esp[0] + ".bsa";
								FileRW.savetofile(data);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

			for (File f : yourDir.listFiles()) {

				if (f.isFile()) {

					check = false;
					FilesData data = new FilesData();
					String[] esp = f.getName().split("\\.");
					if (f.getName().equals("Bloodmoon.esm")) {

						try {
							int i = 0;
							while (i < loadedFileCheck.size() && check == false) {
								if (loadedFileCheck.get(i).name.equals(f
										.getName()))
									check = true;
								else
									check = false;
								i++;
							}
							if (check == false) {
								data.name = f.getName();
								data.nameBsa = esp[0] + ".bsa";
								FileRW.savetofile(data);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
			for (File f : yourDir.listFiles()) {

				if (f.isFile()) {

					check = false;
					FilesData data = new FilesData();
					String[] esp = f.getName().split("\\.");
					if (f.getName().equals("Tribunal.esm")) {

						try {
							int i = 0;
							while (i < loadedFileCheck.size() && check == false) {
								if (loadedFileCheck.get(i).name.equals(f
										.getName()))
									check = true;
								else
									check = false;
								i++;
							}
							if (check == false) {
								data.name = f.getName();
								data.nameBsa = esp[0] + ".bsa";
								FileRW.savetofile(data);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

			for (File f : yourDir.listFiles()) {

				if (f.isFile()) {

					check = false;
					FilesData data = new FilesData();
					String[] esp = f.getName().split("\\.");
					if (esp[1].equals("esm")
							&& !f.getName().equals("Bloodmoon.esm")
							&& !f.getName().equals("Morrowind.esm")
							&& !f.getName().equals("Tribunal.esm")) {

						try {
							int i = 0;
							while (i < loadedFileCheck.size() && check == false) {
								if (loadedFileCheck.get(i).name.equals(f
										.getName()))
									check = true;
								else
									check = false;
								i++;
							}
							if (check == false) {
								data.name = f.getName();
								data.nameBsa = esp[0] + ".bsa";
								FileRW.savetofile(data);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

			for (File f : yourDir.listFiles()) {

				if (f.isFile()) {

					check = false;
					FilesData data = new FilesData();
					String[] esp = f.getName().split("\\.");
					if (esp[1].equals("esp")) {

						try {
							int i = 0;
							while (i < loadedFileCheck.size() && check == false) {
								if (loadedFileCheck.get(i).name.equals(f
										.getName()))
									check = true;
								else
									check = false;
								i++;
							}
							if (check == false) {
								data.name = f.getName();
								data.nameBsa = esp[0] + ".bsa";
								FileRW.savetofile(data);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}

			loadedFile = null;

			try {
				loadedFile = FileRW.loadFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			context = this;

			ListView listView = (ListView) findViewById(R.id.listView1);

			listView.setAdapter(new Adapter());
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "no data files found",
					Toast.LENGTH_LONG).show();
			finish();
		}

	}

	public void savePlugins(View v) throws IOException {
		List<FilesData> plugins = null;

		try {
			plugins = FileRW.loadFile();

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

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			rowView = inflater.inflate(R.layout.rowlistview, parent, false);

			TextView data = (TextView) rowView.findViewById(R.id.textView1);
			TextView bsa = (TextView) rowView.findViewById(R.id.textViewBsa);
			TextView enabled = (TextView) rowView
					.findViewById(R.id.textViewenabled);

			final CheckBox Box = (CheckBox) rowView
					.findViewById(R.id.checkBoxenable);
			enabled.setText(String.valueOf(loadedFile.get(position).enabled));

			if (loadedFile.get(position).enabled == 1)
				Box.setChecked(true);

			Box.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (Box.isChecked()) {
						FilesData data = new FilesData();
						data.enabled = 1;
						FileRW.pos = position;
						try {
							FileRW.updatetofile(data);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {

						FilesData data = new FilesData();
						data.enabled = 0;
						FileRW.pos = position;
						try {
							FileRW.updatetofile(data);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				}

			});

			data.setText(loadedFile.get(position).name);
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

			return loadedFile.size();
			// return 3;

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

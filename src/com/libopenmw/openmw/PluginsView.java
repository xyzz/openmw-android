package com.libopenmw.openmw;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.libopenmw.openmw.FileRW.FilesData;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

		loadedFileCheck = null;
		try {
			loadedFileCheck = FileRW.loadFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		File sdCardRoot = Environment.getExternalStorageDirectory();
		File yourDir = new File(sdCardRoot, "/morrowind/data");
		for (File f : yourDir.listFiles()) {

			if (f.isFile()) {

				check = false;
				FilesData data = new FilesData();
				String[] esp = f.getName().split("\\.");
				if (esp[1].equals("esm") || esp[1].equals("esp")) {

					data.name = f.getName();

					try {
						int i = 0;
						while (i < loadedFileCheck.size() && check == false) {
							if (loadedFileCheck.get(i).name.equals(f.getName()))
								check = true;
							else
								check = false;
							i++;
						}
						if (check == false)
							FileRW.savetofile(data);
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

			data.setText(loadedFile.get(position).name);
			return rowView;

			/*
			 * rowView.findViewById(R.id.button1).setOnClickListener( new
			 * OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * 
			 * Intent dialog = new Intent(getActivity(), EditFile.class);
			 * 
			 * dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 * 
			 * EditFile.notebuffer = loadedFile.get(position).note;
			 * 
			 * EditFile.pos = position;
			 * 
			 * getActivity().startActivity(dialog);
			 * 
			 * }
			 * 
			 * });
			 * 
			 * rowView.findViewById(R.id.button2).setOnClickListener( new
			 * OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * 
			 * }
			 * 
			 * });
			 * 
			 * note.setText(loadedFile.get(position).note);
			 * 
			 * return rowView;
			 */

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

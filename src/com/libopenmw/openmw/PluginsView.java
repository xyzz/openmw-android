package com.libopenmw.openmw;

import java.io.IOException;
import java.util.List;

import com.libopenmw.openmw.FileRW.FilesData;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PluginsView extends Activity {

	public Context context;
	public static List<FilesData> loadedFile;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview);

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

			TextView note = (TextView) rowView.findViewById(R.id.textView1);

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

		//	return loadedFile.size();
			return 3;

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

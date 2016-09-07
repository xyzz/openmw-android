package fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.libopenmw.openmw.FileChooser;
import com.libopenmw.openmw.R;
import com.mobeta.android.dslv.DragSortListView;

import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import constants.Constants;
import ui.files.ConfigsFileStorageHelper;
import json.JsonReader;
import json.JsonReader.PluginInfo;
import plugins.PluginReader;
import ui.files.PreferencesHelper;

public class FragmentPlugins extends Fragment {

    private Adapter adapter;
    private int deletePos = -1;
    private static final int REQUEST_PATH = 12;
    private static FragmentPlugins Instance = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instance = this;
        View rootView = inflater.inflate(R.layout.listview, container, false);
        PreferencesHelper.getPrefValues(this.getActivity());
        setupViews(rootView);
        return rootView;
    }


    private void setupViews(View rootView) {

        DragSortListView listView = (DragSortListView) rootView
                .findViewById(R.id.listView1);
        adapter = new Adapter();
        listView.setAdapter(adapter);

        listView.setDropListener(onDrop);
        listView.setRemoveListener(onRemove);


    }


    public void disableMods() {
        showModDialog(false, "Do you want to disable all mods ?");
    }

    public void enableMods() {
        showModDialog(true, "Do you want to enable all mods ?");
    }

    public void importMods() {
        try {
            FileChooser.isDirMode = false;
            getFileOrFolder();

        } catch (Exception e) {

        }

    }

    public void exportMods() {
        try {
            FileChooser.isDirMode = true;
            getFileOrFolder();
        } catch (Exception e) {
        }
    }

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            deletePos = which;
            showDialod();
        }
    };


    private void showModDialog(final boolean isModEnable, String message) {
        new MaterialDialog.Builder(FragmentPlugins.this.getActivity())
                .content(message)
                .positiveText("OK")
                .negativeText("Cancel").callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                changeModsStatus(isModEnable);
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                reloadAdapter();
            }
        }).show();


    }


    private void showDependenciesDialog(final int pos) {

        String dependencies = "";
        try {
            dependencies = PluginReader.read(Constants.APPLICATION_DATA_STORAGE_PATH + "/"
                    + pluginsList.get(pos).name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new MaterialDialog.Builder(FragmentPlugins.this.getActivity())
                .title("Dependencies")
                .content(dependencies)
                .negativeText("Close").show();

    }


    private void changeModsStatus(boolean isModEnable) {
        for (int i = 0; i < pluginsList.size(); i++)
            if (isModEnable)
                pluginsList.get(i).enabled = 1;
            else
                pluginsList.get(i).enabled = 0;
        reloadAdapter();
        savePluginsData(ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH + "/files.json");
    }

    private void reloadAdapter() {
        adapter.notifyDataSetChanged();
        try {
            savePlugins();

        } catch (Exception e) {

        }

    }


    public void getFileOrFolder() {
        Intent intent = new Intent(FragmentPlugins.this.getActivity(), FileChooser.class);
        startActivityForResult(intent, REQUEST_PATH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PATH) {
            if (resultCode == Activity.RESULT_OK) {
                exportImportMods(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    private void exportImportMods(Intent data) {
        String curPath = "";

        if (!FileChooser.isDirMode) {
            curPath = data.getStringExtra("GetFileName");
            if (curPath.endsWith(".json"))
                try {
                    loadPlugins(curPath);
                    reloadAdapter();

                } catch (Exception e) {

                }
        } else {
            curPath = data.getStringExtra("GetDir");
            try {
                savePluginsData(curPath + "/files.json");
                Toast.makeText(
                        FragmentPlugins.this.getActivity().getApplicationContext(),
                        "file exported to " + curPath + "/files.json", Toast.LENGTH_LONG).show();
            } catch (Exception e) {

            }

        }
    }


    private void showDialod() {
        new MaterialDialog.Builder(FragmentPlugins.this.getActivity())
                .content("Do you want to delete " + pluginsList.get(deletePos).name
                        + " ?")
                .positiveText("OK")
                .negativeText("Cancel").callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                deletePlugin();
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                reloadAdapter();
            }
        }).show();

    }

    private void deletePlugin() {
        File inputfile = new File(Constants.APPLICATION_DATA_STORAGE_PATH + "/"
                + pluginsList.get(deletePos).name);
        if (inputfile.exists())
            inputfile.delete();
        pluginsList.remove(deletePos);
        savePluginsData(ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH + "/files.json");
        reloadAdapter();

    }




    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            PluginInfo item = pluginsList.get(from);

            pluginsList.remove(from);
            pluginsList.add(to, item);
            reloadAdapter();
            savePluginsData(ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH + "/files.json");
        }
    };

    private void savePluginsData(final String path) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    JsonReader.saveFile(pluginsList, path);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public class Adapter extends BaseAdapter

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

            LayoutInflater inflater = (LayoutInflater) FragmentPlugins.this
                    .getActivity().getSystemService(
                            Context.LAYOUT_INFLATER_SERVICE);

            rowView = inflater.inflate(R.layout.rowlistview, parent, false);

            TextView data = (TextView) rowView.findViewById(R.id.textView1);

            final CheckBox Box = (CheckBox) rowView
                    .findViewById(R.id.checkBoxenable);


            if (pluginsList.get(position).enabled == 1) {
                Box.setChecked(true);
            }

            Button dependButton = (Button) rowView.findViewById(R.id.Dependencies);
            dependButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDependenciesDialog(position);

                }
            });


            Box.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (Box.isChecked()) {

                        pluginsList.get(position).enabled = 1;

                        reloadAdapter();

                        savePluginsData(ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH + "/files.json");
                    } else {

                        pluginsList.get(position).enabled = 0;
                        reloadAdapter();

                        savePluginsData(ConfigsFileStorageHelper.CONFIGS_FILES_STORAGE_PATH + "/files.json");

                    }
                }

            });

            data.setText(pluginsList.get(position).name);
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

            return pluginsList.size();

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

    }

}

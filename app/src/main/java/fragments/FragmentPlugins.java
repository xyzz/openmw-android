package fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
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
import ui.files.ParseJson;
import ui.files.ParseJson.FilesData;
import ui.files.PluginReader;
import ui.files.PreferencesHelper;

public class FragmentPlugins extends Fragment {

    private List<FilesData> Plugins;
    private Adapter adapter;
    private int deletePos = -1;
    private static final int REQUEST_PATH = 12;
    public static FragmentPlugins instance = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        instance = this;
        View rootView = inflater.inflate(R.layout.listview, container, false);

        PreferencesHelper.getPrefValues(this.getActivity());
        loadPlugins(Constants.configsPath + "/files.json");
        setupViews(rootView);

        try {
            savePlugins();

        } catch (Exception e) {

        }

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
            dependencies = PluginReader.read(Constants.dataPath + "/"
                    + Plugins.get(pos).name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new MaterialDialog.Builder(FragmentPlugins.this.getActivity())
                .title("Dependencies")
                .content(dependencies)
                .negativeText("Close").show();

    }


    private void changeModsStatus(boolean isModEnable) {
        for (int i = 0; i < Plugins.size(); i++)
            if (isModEnable)
                Plugins.get(i).enabled = 1;
            else
                Plugins.get(i).enabled = 0;
        reloadAdapter();
        savePluginsData(Constants.configsPath + "/files.json");
    }

    private void reloadAdapter() {
        adapter.notifyDataSetChanged();
        try {
            savePlugins();

        } catch (Exception e) {

        }

    }


    private void loadPlugins(String path) {
        try {
            Plugins = ParseJson.loadFile(path);
            if (Plugins == null)
                Plugins = new ArrayList<ParseJson.FilesData>();

            File yourDir = new File(Constants.dataPath);

            checkFilesDeleted(yourDir);

            addNewFiles(yourDir);


        } catch (Exception e) {
            Toast.makeText(
                    FragmentPlugins.this.getActivity().getApplicationContext(),
                    "data files not found", Toast.LENGTH_LONG).show();
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
        super.onActivityResult(requestCode,resultCode,data);

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
                .content("Do you want to delete " + Plugins.get(deletePos).name
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
        File inputfile = new File(Constants.dataPath + "/"
                + Plugins.get(deletePos).name);
        if (inputfile.exists())
            inputfile.delete();
        Plugins.remove(deletePos);
        savePluginsData(Constants.configsPath + "/files.json");
        reloadAdapter();

    }

    private void savePlugins() throws IOException {

        try {

            FileWriter writer = new FileWriter(Constants.configsPath
                    + "/openmw/openmw.cfg");

            int i = 0;
            while (i < Plugins.size()) {

                if (Plugins.get(i).enabled == 1) {
                    writer.write("content= " + Plugins.get(i).name + "\n");

                    if (checkBsaExists(Constants.dataPath + "/"
                            + Plugins.get(i).nameBsa))
                        writer.write("fallback-archive= "
                                + Plugins.get(i).nameBsa + "\n");

                    writer.flush();
                }
                i++;

            }
            writer.close();


        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private boolean checkBsaExists(String path) {
        File inputfile = new File(path);
        return inputfile.exists();

    }

    private void checkFilesDeleted(File yourDir) throws JSONException,
            IOException {
        int deletedFilesCount = 0;
        int i = 0;
        List<FilesData> tmp = ParseJson.loadFile(Constants.configsPath + "/files.json");
        for (i = 0; i < tmp.size(); i++) {
            boolean fileDeleted = true;

            for (File f : yourDir.listFiles()) {

                if (f.isFile() && f.getName().endsWith(tmp.get(i).name)) {

                    fileDeleted = false;
                    break;

                } else
                    fileDeleted = true;

            }

            if (fileDeleted) {
                Plugins.remove(i - deletedFilesCount);
                deletedFilesCount++;
            }

        }
        if (Plugins.size() < i)
            savePluginsData(Constants.configsPath + "/files.json");

    }


    private void addNewFiles(File yourDir) throws JSONException, IOException {
        int lastEsmPos = 0;

        for (int i = 0; i < Plugins.size(); i++) {
            if (Plugins.get(i).name.endsWith(".esm")
                    || Plugins.get(i).name.endsWith(".ESM"))
                lastEsmPos = i;
            else
                break;
        }

        for (File f : yourDir.listFiles()) {

            boolean newPlugin = true;
            for (FilesData data : Plugins) {
                if (f.isFile() && f.getName().endsWith(data.name)) {

                    newPlugin = false;
                    break;

                } else
                    newPlugin = true;

            }
            if (newPlugin) {
                FilesData pluginData = new FilesData();

                pluginData.name = f.getName();
                pluginData.nameBsa = f.getName().split("\\.")[0] + ".bsa";
                if (f.getName().endsWith(".esm")
                        || f.getName().endsWith(".ESM")) {
                    Plugins.add(lastEsmPos, pluginData);
                    lastEsmPos++;
                } else if (f.getName().endsWith(".esp")
                        || f.getName().endsWith(".ESP")
                        || f.getName().endsWith(".omwgame")
                        || f.getName().endsWith(".omwaddon")) {
                    Plugins.add(pluginData);
                }

            }

        }

        savePluginsData(Constants.configsPath + "/files.json");
    }

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            FilesData item = Plugins.get(from);


            Plugins.remove(from);

            Plugins.add(to, item);
            reloadAdapter();
            savePluginsData(Constants.configsPath + "/files.json");
        }
    };

    private void savePluginsData(final String path) {

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ParseJson.saveFile(Plugins, path);
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



            if (Plugins.get(position).enabled == 1) {
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

                        Plugins.get(position).enabled = 1;

                        reloadAdapter();

                        savePluginsData(Constants.configsPath + "/files.json");
                    } else {

                        Plugins.get(position).enabled = 0;
                        reloadAdapter();

                        savePluginsData(Constants.configsPath + "/files.json");

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

    }

}

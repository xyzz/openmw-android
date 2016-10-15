package ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.libopenmw.openmw.FileChooser;
import com.libopenmw.openmw.R;
import com.mobeta.android.dslv.DragSortListView;
import java.io.IOException;
import constants.Constants;
import dialog.MaterialDialogInterface;
import dialog.MaterialDialogManager;
import game.GameState;
import plugins.PluginReader;
import plugins.PluginsAdapter;
import plugins.PluginsStorage;
import plugins.PluginsUtils;
import plugins.bsa.BsaWriter;
import ui.files.PreferencesHelper;

public class FragmentPlugins extends Fragment {

    private PluginsAdapter adapter;
    private PluginsStorage pluginsStorage;
    protected MaterialDialogManager materialDialogManager;
    private static final int REQUEST_PATH = 12;
    private static FragmentPlugins Instance = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instance = this;
        materialDialogManager = new MaterialDialogManager(FragmentPlugins.this.getActivity());
        pluginsStorage = new PluginsStorage(this.getActivity());
        View rootView = inflater.inflate(R.layout.listview, container, false);
        PreferencesHelper.getPrefValues(this.getActivity());
        setupViews(rootView);
        return rootView;
    }

    public void savePluginsDataToDisk() {
        if (pluginsStorage != null && pluginsStorage.getPluginsList() != null) {
            pluginsStorage.savePluginsData("");
            if (!BsaWriter.getSaveAllBsaFilesValue(FragmentPlugins.this.getActivity())) {
                PluginsUtils.savePlugins(pluginsStorage.getPluginsList(), FragmentPlugins.this.getActivity());
            } else {
                BsaWriter.saveAllBsaFiles(FragmentPlugins.this.getActivity(), true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!GameState.getGameState()) {
            savePluginsDataToDisk();
        }
        Instance = null;
    }

    public static FragmentPlugins getInstance() {
        return Instance;
    }

    public PluginsStorage getPluginsStorage() {
        return pluginsStorage;
    }

    private void setupViews(View rootView) {
        DragSortListView listView = (DragSortListView) rootView
                .findViewById(R.id.listView1);
        adapter = new PluginsAdapter(FragmentPlugins.this);
        listView.setAdapter(adapter);
        listView.setDropListener(onDrop);
    }

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (pluginsStorage != null) {
                pluginsStorage.replacePlugins(from, to);
                reloadAdapter();
            }
        }
    };


    public void disableMods() {
        showModDialog(false, "Do you want to disable all mods ?");
    }

    public void enableMods() {
        showModDialog(true, "Do you want to enable all mods ?");
    }

    private void showModDialog(final boolean isModEnable, String message) {
        MaterialDialogInterface materialDialogInterface = new MaterialDialogInterface() {
            @Override
            public void onPositiveButtonPressed() {
                changeModsStatus(isModEnable);
            }

            @Override
            public void onNegativeButtonPressed() {
                reloadAdapter();
            }
        };
        materialDialogManager.showDialog("", message, "Cancel", "OK", materialDialogInterface);
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


    public void showDependenciesDialog(final int pos) {
        String dependencies = "";
        try {
            dependencies = PluginReader.read(Constants.APPLICATION_DATA_STORAGE_PATH + "/"
                    + pluginsStorage.getPluginsList().get(pos).name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        materialDialogManager.showMessageDialogBox("Dependencies", dependencies, "Close");
    }


    private void changeModsStatus(boolean needEnableMods) {
        if (pluginsStorage != null) {
            pluginsStorage.updatePluginsStatus(needEnableMods);
        }
        reloadAdapter();
    }

    private void reloadAdapter() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
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
                    pluginsStorage.loadPlugins(curPath);
                    reloadAdapter();
                } catch (Exception e) {

                }
        } else {
            curPath = data.getStringExtra("GetDir");
            try {
                pluginsStorage.savePluginsData(curPath + "/files.json");
                Toast.makeText(
                        FragmentPlugins.this.getActivity().getApplicationContext(),
                        "file exported to " + curPath + "/files.json", Toast.LENGTH_LONG).show();
            } catch (Exception e) {

            }

        }
    }
}

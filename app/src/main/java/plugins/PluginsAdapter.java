package plugins;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.libopenmw.openmw.R;

import ui.fragments.FragmentPlugins;

public class PluginsAdapter extends BaseAdapter {
    private FragmentPlugins fragmentPlugins;
    public View rowView;

    public PluginsAdapter(FragmentPlugins fragmentPlugins) {
        this.fragmentPlugins = fragmentPlugins;
    }

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

        LayoutInflater inflater = (LayoutInflater) fragmentPlugins.getActivity().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.rowlistview, parent, false);
        TextView data = (TextView) rowView.findViewById(R.id.textView1);
        final CheckBox Box = (CheckBox) rowView
                .findViewById(R.id.checkBoxenable);

        if (fragmentPlugins.getPluginsStorage().getPluginsList().get(position).enabled) {
            Box.setChecked(true);
        }
        Button dependButton = (Button) rowView.findViewById(R.id.Dependencies);
        dependButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentPlugins.showDependenciesDialog(position);
            }
        });
        Box.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragmentPlugins.getPluginsStorage().updatePluginStatus(position, Box.isChecked());
                PluginsAdapter.this.notifyDataSetChanged();
            }
        });
        data.setText(fragmentPlugins.getPluginsStorage().getPluginsList().get(position).name);
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
        return fragmentPlugins.getPluginsStorage().getPluginsList().size();
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
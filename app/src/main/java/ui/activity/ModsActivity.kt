package ui.activity

import com.libopenmw.openmw.R

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.TabLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_mods.*
import mods.*


class ModsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mods)

        // Switch tabs between plugins/resources
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                flipper.displayedChild = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })

        // Set up adapters for the lists
        setupModList(findViewById(R.id.list_mods), ModType.Plugin,
            arrayOf("esm", "esp", "omwaddon"))
        setupModList(findViewById(R.id.list_resources), ModType.Resource,
            arrayOf("bsa"))
    }

    /**
     * Connects a user-interface RecyclerView to underlying mod data on the disk
     * @param list The list displayed to the user
     * @param type Type of the mods this list will contain
     * @param extensions Extension filter for the mods
     */
    private fun setupModList(list: RecyclerView, type: ModType, extensions: Array<String>) {
        val dataFiles = PreferenceManager.getDefaultSharedPreferences(this)
            .getString("data_files", "")

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        list.layoutManager = linearLayoutManager

        // Set up the adapter using the specified ModsCollection
        val adapter = ModsAdapter(ModsCollection(type, dataFiles, extensions, database))

        // Set up the drag-and-drop callback
        val callback = ModMoveCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(list)

        adapter.touchHelper = touchHelper

        list.adapter = adapter
    }
}

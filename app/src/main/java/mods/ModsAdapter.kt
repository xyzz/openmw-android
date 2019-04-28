package mods

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.libopenmw.openmw.R
import java.util.*

/**
 * An adapter to put a ModsCollection into a UI list
 */
class ModsAdapter(private val collection: ModsCollection) : RecyclerView.Adapter<ModsAdapter.ModViewHolder>() {

    lateinit var touchHelper: ItemTouchHelper

    /**
     * A row representation of a mod
     */
    inner class ModViewHolder(internal var rowView: View) : RecyclerView.ViewHolder(rowView) {
        val mTitle: TextView = rowView.findViewById(R.id.txtTitle)
        val mHandle: ImageView = rowView.findViewById(R.id.handle)
        val mCheckbox: CheckBox = rowView.findViewById(R.id.modCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mod_item, parent, false)
        return ModViewHolder(itemView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ModViewHolder, position: Int) {
        val mod = collection.mods[position]
        holder.mTitle.text = mod.filename
        holder.mHandle.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchHelper.startDrag(holder)
            }
            false
        }
        holder.mCheckbox.isChecked = mod.enabled
        holder.mCheckbox.setOnClickListener {
            mod.enabled = (it as CheckBox).isChecked
            mod.dirty = true
            collection.update()
        }
    }

    override fun getItemCount(): Int {
        return collection.mods.size
    }

    private fun swapMods(from: Int, to: Int) {
        // Swap the orders
        val tmp = collection.mods[from].order
        collection.mods[from].order = collection.mods[to].order
        collection.mods[to].order = tmp

        // Swap the mods inside the list
        Collections.swap(collection.mods, from, to)

        // Mark mods as dirty to update when the user releases currently dragged mod
        collection.mods[from].dirty = true
        collection.mods[to].dirty = true
    }

    fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                swapMods(i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                swapMods(i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    fun onRowSelected(modViewHolder: ModViewHolder) {
        modViewHolder.rowView.setBackgroundColor(Color.LTGRAY)
    }

    fun onRowClear(modViewHolder: ModViewHolder) {
        modViewHolder.rowView.setBackgroundColor(Color.WHITE)
        collection.update()
    }
}

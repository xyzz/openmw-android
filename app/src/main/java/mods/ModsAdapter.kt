package mods

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.libopenmw.openmw.R

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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mod_item, parent, false)
        return ModViewHolder(itemView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ModViewHolder, position: Int) {
        holder.mTitle.text = collection.mods[position].filename
        holder.mHandle.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchHelper.startDrag(holder)
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return collection.mods.size
    }

    fun onRowMoved(fromPosition: Int, toPosition: Int) {
//        if (fromPosition < toPosition) {
//            for (i in fromPosition until toPosition) {
//                Collections.swap(data, i, i + 1)
//            }
//        } else {
//            for (i in fromPosition downTo toPosition + 1) {
//                Collections.swap(data, i, i - 1)
//            }
//        }
        notifyItemMoved(fromPosition, toPosition)
    }

    fun onRowSelected(modViewHolder: ModViewHolder) {
        modViewHolder.rowView.setBackgroundColor(Color.LTGRAY)
    }

    fun onRowClear(modViewHolder: ModViewHolder) {
        modViewHolder.rowView.setBackgroundColor(Color.WHITE)
    }
}

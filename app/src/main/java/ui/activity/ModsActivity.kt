package ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import com.libopenmw.openmw.R

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import java.util.*
import android.view.View
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_mods.*
import android.view.MotionEvent



class RecyclerViewAdapter(private val data: ArrayList<String>) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>(), ItemMoveCallback.ItemTouchHelperContract {

    lateinit var touchHelper: ItemTouchHelper

    inner class MyViewHolder(internal var rowView: View) : RecyclerView.ViewHolder(rowView) {

        val mTitle: TextView = rowView.findViewById(R.id.txtTitle)
        val mHandle: ImageView = rowView.findViewById(R.id.handle)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mod_item, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mTitle.text = data[position]
        holder.mHandle.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                touchHelper.startDrag(holder)
            }
            false
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }


    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(data, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(data, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(myViewHolder: MyViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.LTGRAY)
    }

    override fun onRowClear(myViewHolder: MyViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE)
    }
}

class ItemMoveCallback(private val mAdapter: ItemTouchHelperContract) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }


    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        mAdapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?,
                                   actionState: Int) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder is RecyclerViewAdapter.MyViewHolder) {
                mAdapter.onRowSelected(viewHolder)
            }

        }

        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView?,
                           viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if (viewHolder is RecyclerViewAdapter.MyViewHolder) {
            mAdapter.onRowClear(viewHolder)
        }
    }

    /**
     * Speed up dragging of a list element
     */
    override fun interpolateOutOfBoundsScroll(recyclerView: RecyclerView, viewSize: Int, viewSizeOutOfBounds: Int, totalSize: Int, msSinceStartScroll: Long): Int {
        val direction = Math.signum(viewSizeOutOfBounds.toFloat()).toInt()
        return 20 * direction
    }

    interface ItemTouchHelperContract {

        fun onRowMoved(fromPosition: Int, toPosition: Int)
        fun onRowSelected(myViewHolder: RecyclerViewAdapter.MyViewHolder)
        fun onRowClear(myViewHolder: RecyclerViewAdapter.MyViewHolder)

    }

}


class ModsActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var mAdapter: RecyclerViewAdapter
    var stringArrayList: ArrayList<String> = ArrayList()

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

        recyclerView = findViewById(R.id.mods_list)

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        populateRecyclerView()
    }

    private fun populateRecyclerView() {
        for (i in 1..200) {
            stringArrayList.add("Item $i")
        }

        mAdapter = RecyclerViewAdapter(stringArrayList)

        val callback = ItemMoveCallback(mAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        mAdapter.touchHelper = touchHelper

        recyclerView.adapter = mAdapter
    }
}

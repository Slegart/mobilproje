package com.sefikonurakin_hw2.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sefikonurakin_hw2.R
import com.sefikonurakin_hw2.db.Tasks


class CustomRecyclerViewAdapter(
    private val context: Context,
    private val recyclerItemValues: MutableList<Tasks>,
    private val itemClickListener: OnTaskItemClickListener
) : RecyclerView.Adapter<CustomRecyclerViewAdapter.RecyclerViewItemHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerViewItemHolder {
        // Use viewType to determine which layout to inflate
        val layoutResId = if (viewType == 1) R.layout.recycler_item else R.layout.recycler_item_2

        val inflator = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflator.inflate(layoutResId, viewGroup, false)

        val backgroundColor = if (viewType == 1) Color.GREEN else Color.RED
        itemView.setBackgroundColor(backgroundColor)

        return RecyclerViewItemHolder(itemView)
    }

    override fun onBindViewHolder(myRecyclerViewItemHolder: RecyclerViewItemHolder, position: Int) {
        val item = recyclerItemValues[position]

        myRecyclerViewItemHolder.TaskName.text = item.TaskName
        myRecyclerViewItemHolder.TaskDate.text = item.Date
        myRecyclerViewItemHolder.TaskPriority.text = item.Priority.toString()

        myRecyclerViewItemHolder.parentLayout.setOnClickListener {
            itemClickListener.onTaskItemClick(item, "update")
        }
    }

    override fun getItemCount(): Int {
        return recyclerItemValues.size
    }

    override fun getItemViewType(position: Int): Int {
        // Determine the viewType based on the priority
        return if (recyclerItemValues[position].Priority > 3) 2 else 1
    }

    inner class RecyclerViewItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var parentLayout: LinearLayout
        lateinit var TaskName: TextView
        lateinit var TaskDate: TextView
        lateinit var TaskPriority: TextView

        init {
            parentLayout = itemView.findViewById(R.id.itemLayout)
            TaskName = itemView.findViewById(R.id.task_name)
            TaskDate = itemView.findViewById(R.id.task_date)
            TaskPriority = itemView.findViewById(R.id.task_priority)
        }
    }

    interface OnTaskItemClickListener {
        fun onTaskItemClick(task: Tasks, operation: String)
    }
}


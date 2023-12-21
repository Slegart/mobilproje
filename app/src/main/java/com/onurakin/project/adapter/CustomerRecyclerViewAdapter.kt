package com.onurakin.project.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.onurakin.project.R
import com.onurakin.project.db.Products.Products


class CustomRecyclerViewAdapter(
    private val context: Context,
    private val recyclerItemValues: MutableList<Products>,
    private val itemClickListener: OnTaskItemClickListener
) : RecyclerView.Adapter<CustomRecyclerViewAdapter.RecyclerViewItemHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerViewItemHolder {
        val layoutResId = R.layout.recycler_item

        val inflator = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflator.inflate(layoutResId, viewGroup, false)

        return RecyclerViewItemHolder(itemView)
    }

    override fun onBindViewHolder(myRecyclerViewItemHolder: RecyclerViewItemHolder, position: Int) {
        val item = recyclerItemValues[position]

        myRecyclerViewItemHolder.TaskName.text = item.ProductName
        myRecyclerViewItemHolder.TaskDate.text = item.Date
        myRecyclerViewItemHolder.TaskPriority.text = item.Price.toString()

        myRecyclerViewItemHolder.parentLayout.setOnClickListener {
            itemClickListener.onTaskItemClick(item, "update")
        }
    }

    override fun getItemCount(): Int {
        return recyclerItemValues.size
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
        fun onTaskItemClick(task: Products, operation: String)

    }
    fun updateData(newData: List<Products>) {
        recyclerItemValues.clear()
        recyclerItemValues.addAll(newData)
        notifyDataSetChanged()
        Log.d("CustomRecyclerViewAdapter", "Data updated. New size: ${recyclerItemValues.size}")
    }
}

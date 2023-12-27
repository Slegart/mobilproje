package com.onurakin.project.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.onurakin.project.R
import com.onurakin.project.db.Products.Products

class CartItemsRecycleViewAdapter(
    val recyclerItemValues: MutableList<Products>,
    //private var itemList: MutableList<Products>,
    private val itemClickListener: OnTaskItemClickListener
): RecyclerView.Adapter<CartItemsRecycleViewAdapter.RecyclerViewItemHolder>() {

    fun updateData(newItems: List<Products>) {
        recyclerItemValues.clear()
        recyclerItemValues.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewItemHolder {
        val item = R.layout.fragment_item

        val inflator = LayoutInflater.from(parent.context)
        val itemView: View = inflator.inflate(item, parent, false)

        return RecyclerViewItemHolder(itemView)
    }

    override fun getItemCount(): Int {
        Log.d("BuyItemsRecycleViewAdapter", "getItemCount: ${recyclerItemValues.size}")
        return recyclerItemValues.size
    }

    fun getData(): List<Products> {
        return recyclerItemValues
    }

    override fun onBindViewHolder(
        holder: RecyclerViewItemHolder,
        position: Int
    ) {
        val product = recyclerItemValues[position]
        Log.d("BuyItemsRecycleViewAdapter", "onBindViewHolder: $product")
        holder.bind(product)

        holder.itemView.setOnClickListener {
            itemClickListener.onItemClick(product)
        }
    }
    interface OnTaskItemClickListener {
        fun onItemClick(product: Products)
    }
    inner class RecyclerViewItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var namefield: TextView
        lateinit var pricefield: TextView
        init {
            namefield = itemView.findViewById(R.id.nameoffragment)
            pricefield = itemView.findViewById(R.id.priceofFragment)
        }
        fun bind(product: Products) {
            namefield.text = product.ProductName
            pricefield.text = product.Price.toString()
        }
    }
    fun removeItem(item: Products) {
        recyclerItemValues.remove(item)
        notifyDataSetChanged()
    }
}

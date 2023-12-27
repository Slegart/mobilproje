package com.onurakin.project.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onurakin.project.R
import com.onurakin.project.adapter.BuyItemsRecycleViewAdapter
import com.onurakin.project.db.Products.Products

class BuyItemsFragment : Fragment(), BuyItemsRecycleViewAdapter.OnTaskItemClickListener {

    private lateinit var buyItemsRecyclerAdapter: BuyItemsRecycleViewAdapter
    private var itemMoveListener: OnItemMoveListener? = null

    interface OnItemMoveListener {
        fun onItemMoveToOtherFragment(products: List<Products>)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_buy_items, container, false)
        val args = arguments
        val deletedProducts = args?.getSerializable("DeletedProducts") as? List<Products>

        buyItemsRecyclerAdapter = BuyItemsRecycleViewAdapter(mutableListOf(), this)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleviewbuyid)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = buyItemsRecyclerAdapter

        itemMoveListener = activity as? OnItemMoveListener

        if (deletedProducts != null) {
            val mutableDeletedProducts: MutableList<Products> = deletedProducts.toMutableList()
            updateBuyItems(mutableDeletedProducts)
        }

        return view
    }

    fun updateBuyItems(deletedProducts: List<Products>) {
        buyItemsRecyclerAdapter.updateData(deletedProducts)
    }

    override fun onItemClick(products: Products) {
        itemMoveListener?.onItemMoveToOtherFragment(listOf(products))
        buyItemsRecyclerAdapter.removeItem(products)
    }

    fun getBuyItems(): List<Products> {
        return buyItemsRecyclerAdapter.getData()
    }

    fun removeItem(products: Products) {
        buyItemsRecyclerAdapter.removeItem(products)
    }
}

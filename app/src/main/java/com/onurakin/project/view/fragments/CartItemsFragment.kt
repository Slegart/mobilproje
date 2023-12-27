package com.onurakin.project.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.onurakin.project.R
import com.onurakin.project.adapter.CartItemsRecycleViewAdapter
import com.onurakin.project.db.Products.Products

class CartItemsFragment : Fragment(), CartItemsRecycleViewAdapter.OnTaskItemClickListener {

    private lateinit var cartItemsRecycleViewAdapter: CartItemsRecycleViewAdapter
    private var itemMoveListener: OnItemMoveListener? = null

    interface OnItemMoveListener {
        fun onItemMoveToOtherFragment(products: List<Products>)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart_items, container, false)

        cartItemsRecycleViewAdapter = CartItemsRecycleViewAdapter(mutableListOf(), this)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleviewcartid)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = cartItemsRecycleViewAdapter

        itemMoveListener = activity as? OnItemMoveListener

        return view
    }

    fun updateCartItems(deletedProducts: List<Products>) {
        cartItemsRecycleViewAdapter.updateData(deletedProducts)
    }

    override fun onItemClick(products: Products) {
        itemMoveListener?.onItemMoveToOtherFragment(listOf(products))
        cartItemsRecycleViewAdapter.removeItem(products)
    }

    fun getCartItems(): List<Products> {
        return cartItemsRecycleViewAdapter.getData()
    }
    fun removeItem(products: Products) {
        cartItemsRecycleViewAdapter.removeItem(products)
    }
}

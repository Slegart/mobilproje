package com.onurakin.project.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.room.Room
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.onurakin.project.databinding.ActivityCartBinding
import com.onurakin.project.db.Products.ProductRoomDatabase
import com.onurakin.project.db.Products.Products
import com.onurakin.project.view.fragments.BuyItemsFragment
import com.onurakin.project.view.fragments.CartItemsFragment
import com.onurakin.project.worker.WorkerClass
import com.sefikonurakin_hw2.util.Constants


class CartActivity : FragmentActivity(), CartItemsFragment.OnItemMoveListener, BuyItemsFragment.OnItemMoveListener {
    lateinit var binding: ActivityCartBinding
    lateinit var intent_cart: Intent
    lateinit var deletedProducts: ArrayList<Products>
    lateinit var CartItemsFragment: CartItemsFragment
    lateinit var BuyItemsFragment: BuyItemsFragment
    lateinit var buyItemsButton:Button
    lateinit var workRequest: OneTimeWorkRequest
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CartItemsFragment = CartItemsFragment()
        BuyItemsFragment = BuyItemsFragment()

        deletedProducts = (intent.getSerializableExtra("DeletedProducts") as? ArrayList<Products>)!!

        if (deletedProducts != null) {
            Log.d("CartActivity", "not null")
            for (product in deletedProducts) {
                Log.d("CartActivity", "Product: $product")
            }
            val args = Bundle().apply {
                putSerializable("DeletedProducts", deletedProducts)
            }
            loadFrag(BuyItemsFragment, binding.FragmentBuyItems.id, args)
            loadFrag(CartItemsFragment, binding.FragmentCartItems.id, args)
        }

        binding.apply {
            val gestureDetector = GestureDetector(this@CartActivity, object : GestureDetector.SimpleOnGestureListener() {
                override fun onLongPress(e: MotionEvent) {
                    handleBuyItemsButtonClick()
                }
            })
            buyItemsButton.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }

            binding.returnbutton.setOnClickListener {
                RestoreNonBougtItems()
                val intent = Intent(this@CartActivity, MainActivity::class.java)
                intent.putExtra("isLoggedIn", false)
                startActivity(intent)
                finish()
            }
        }
    }

    fun RestoreNonBougtItems(){
        //Log.d("statues restore elements", "Cart Item: $deletedProducts")
        val BuyItemsFragment = supportFragmentManager.findFragmentById(binding.FragmentBuyItems.id) as? BuyItemsFragment
        val BuyItems = BuyItemsFragment?.getBuyItems()
        val CartItemsFragment = supportFragmentManager.findFragmentById(binding.FragmentCartItems.id) as? CartItemsFragment
        Log.d("staus_item",CartItemsFragment.toString())
        val cartItems = CartItemsFragment?.getCartItems()
        Log.d("statues restore elements", "Cart Item: $cartItems")
        Log.d("statues restore elements", "Buy Item: $BuyItems")
        //if(cartItems != null)
        if (cartItems != null) {
            Log.d("statues restore elements", "Cart Item not null")
            for (item in cartItems) {
                Log.d("statues restore elements", "before: $item")
                ProjectDB.productsDAO().updateInCartStatus(item.id, false)
                Log.d("statues restore elements", "after: $item")
            }
        }
       // if(BuyItems != null)
        if (BuyItems != null) {
            Log.d("statues restore elements", "buy Item not null")
            for (item in BuyItems) {
                Log.d("statues restore elements", "before: $item")
                ProjectDB.productsDAO().updateInCartStatus(item.id, false)
                Log.d("statues restore elements", "after: $item")
            }
        }

    }
    private val ProjectDB: ProductRoomDatabase by lazy {
        Room.databaseBuilder(this, ProductRoomDatabase::class.java, Constants.DATABASENAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun handleBuyItemsButtonClick() {
        val cartItemsFragment = supportFragmentManager.findFragmentById(binding.FragmentCartItems.id) as? CartItemsFragment
        val cartItems = cartItemsFragment?.getCartItems()

        var totalcost = 0
        for (item in cartItems!!) {
            totalcost += item.Price
        }
        var usersmoney = ProjectDB.usersDAO().getUserById(LoginScreen.currentUserId).Money
        //Log.d("mmm", "usersmoney: $usersmoney")
        if(totalcost > usersmoney){
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Not Enough Money")
            builder.setMessage("You don't have enough money to buy these items")
            builder.setPositiveButton("OK") { dialog, which ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            builder.show()

            for (item in cartItems) {
                ProjectDB.productsDAO().updateInCartStatus(item.id, false)
            }

            return
        }
        else
        {
            if (cartItems != null) {
                val productIds = cartItems.map { it.id }
                val inputData = Data.Builder().putIntArray("productIds", productIds.toIntArray()).build()
                val workRequest = OneTimeWorkRequest.Builder(WorkerClass::class.java)
                    .setInputData(inputData)
                    .build()
                WorkManager.getInstance(this).enqueue(workRequest)

                //DeleteManually(cartItems)

                for (item in cartItems) {
                    Log.d("CartActivity", "Cart Item: $item")
                }
            }
            usersmoney -= totalcost
            ProjectDB.usersDAO().updateMoney(LoginScreen.currentUserId, usersmoney)
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("remaining money ")
            builder.setMessage("remaining money is $usersmoney")
            builder.setPositiveButton("OK") { dialog, which ->
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            builder.show()
            val intent = Intent(this@CartActivity, MainActivity::class.java)
            intent.putExtra("isLoggedIn", false)
            startActivity(intent)
            finish()
        }

    }

    fun DeleteManually(cartItems: List<Products>){
        for (item in cartItems) {
            Log.d("deleteitem", "Cdeleteitem: $item")
            ProjectDB.productsDAO().deleteTaskById(item.id)
        }
    }
    fun loadFrag(dynamicFragment: Fragment, id: Int, args: Bundle? = null) {
        val fm: FragmentManager = supportFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()
        dynamicFragment.arguments = args
        ft.replace(id, dynamicFragment)
        ft.commit()
    }
    override fun onItemMoveToOtherFragment(products: List<Products>) {

        val cartItemsFragment = supportFragmentManager.findFragmentById(binding.FragmentCartItems.id) as? CartItemsFragment
        val existingCartItems = cartItemsFragment?.getCartItems()


        if (existingCartItems != null) {
            val mergedItems = existingCartItems.toMutableList()
            mergedItems.addAll(products)
            cartItemsFragment.updateCartItems(mergedItems)
        }
    }
}

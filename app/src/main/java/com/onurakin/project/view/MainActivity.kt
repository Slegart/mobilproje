package com.onurakin.project.view

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import androidx.room.Room
import com.onurakin.project.databinding.ActivityMainBinding
import com.onurakin.project.db.Products.Products
import com.onurakin.project.db.Products.ProductRoomDatabase
import com.sefikonurakin_hw2.util.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.onurakin.project.R
import com.onurakin.project.Retrofit.ApiClient
import com.onurakin.project.Retrofit.TimeResponse
import com.onurakin.project.Retrofit.TimeService
import com.onurakin.project.adapter.CustomRecyclerViewAdapter
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
class MainActivity : AppCompatActivity(), CustomRecyclerViewAdapter.OnTaskItemClickListener {

    lateinit var binding: ActivityMainBinding
    lateinit var customDialog: Dialog
    lateinit var Date:TextView
    lateinit var cart:Button
    lateinit var mediaPlayer: MediaPlayer
    var DeletedProducts = mutableListOf<Products>()
    var TaskTemp =""
    var DateTemp =""
    var PriorityTemp = 0
    var adapter: CustomRecyclerViewAdapter?=null

    private val ProjectDB: ProductRoomDatabase by lazy {
        Room.databaseBuilder(this, ProductRoomDatabase::class.java, Constants.DATABASENAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val addproducts = intent.getBooleanExtra("addproducts", false)

        if(addproducts){
            addProducts()
        }

        fetchData()
        getData()
        fillSpinner()
        mediaPlayer = MediaPlayer.create(this, R.raw.store_bell)

        binding.recyclerofProducts.setLayoutManager(LinearLayoutManager(this))
        binding.apply {

        }

    }

    private fun fetchData() {
        val timeService = ApiClient.getTimeService()
        val request = timeService.getTime()

        request.enqueue(object : Callback<TimeResponse> {
            override fun onResponse(call: Call<TimeResponse>, response: Response<TimeResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    Log.d("apiretro", "Response: ${data?.datetime}")
                } else {
                    Log.d("apiretro", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<TimeResponse>, t: Throwable) {
                Log.d("apiretro", "Error: ${t.message}")
            }
        })
    }

    fun fillSpinner() {
        val spinner = binding.productTypes

        val productTypes = ProjectDB.productsDAO().getDistinctProductTypes()

        val adapter = CustomRecyclerViewAdapter(this, mutableListOf(), this)
        binding.recyclerofProducts.adapter = adapter

        val initialProducts = ProjectDB.productsDAO().getAllTasks()
        adapter.updateData(initialProducts)

        val spinnerAdapter = ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.text1, productTypes)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout)
        spinner.adapter = spinnerAdapter

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("OnItemSelected", "Selected item at position: $position")
                lastSelectedProductType = parent?.getItemAtPosition(position).toString()
                lastSelectedProductType = lastSelectedProductType
                Log.d("SelectedCategory", lastSelectedProductType.toString())

                val categoryProducts = ProjectDB.productsDAO().getProductsByCategory(lastSelectedProductType.toString())
                Log.d("CategoryProducts", categoryProducts.toString())

                adapter.updateData(categoryProducts)
                displayTasks(categoryProducts.toMutableList())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        binding.cart.setOnClickListener()
        {
            val intent = Intent(this, CartActivity::class.java)
            intent.putExtra("DeletedProducts", ArrayList(DeletedProducts))
            startActivity(intent)

        }

    }





    private fun displayTasks(products: MutableList<Products>) {
        val tasksToDisplay = products.filter { !it.InCart }

        adapter = CustomRecyclerViewAdapter(this, tasksToDisplay.toMutableList(), this)

        binding.recyclerofProducts.adapter = adapter
        adapter?.notifyDataSetChanged()
    }


    fun getData(){
        if(ProjectDB.productsDAO().getAllTasks().isNotEmpty()){
            displayTasks(ProjectDB.productsDAO().getAllTasks())
        }
        else{
            binding.recyclerofProducts.adapter = null
        }
    }

    var lastSelectedProductType: String? = null

    override fun onTaskItemClick(products: Products, operation: String) {
        Log.d("item", products.toString())
        Log.d("operation", operation)
        when (operation) {
            "remove" -> {
                DeletedProducts.add(products)

                products.InCart = true
                ProjectDB.productsDAO().updateInCartStatus(products.id, true)
                ProjectDB.productsDAO().updateTask(products)

                val categoryProducts = lastSelectedProductType?.let {
                    ProjectDB.productsDAO().getProductsByCategory(it)
                } ?: lastSelectedProductType?.let {
                    ProjectDB.productsDAO().getProductsByCategory(it)
                } ?: run {
                    ProjectDB.productsDAO().getAllTasks()
                }
                val categoryProductsMutable = categoryProducts.toMutableList()
                displayTasks(categoryProductsMutable)
            }
        }
    }



    public fun getApi1(): Thread {

        return Thread{
            val url = URL("https://worldtimeapi.org/api/timezone/Europe/Istanbul")
            val connection = url.openConnection() as HttpURLConnection

            if (connection.responseCode == 200) {
                val apiResponse = connection.inputStream
                val inputStreamReader = InputStreamReader(apiResponse, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, JsonObject::class.java)
                val datetime = request.get("datetime").toString()
                val abbreviation = request.get("abbreviation").toString()
                UpdateUI(datetime, abbreviation)
                inputStreamReader.close()
                apiResponse.close()
            }
            else {
                Log.d("api", "Error: ${connection.responseCode}")
            }
        }
    }
    private fun UpdateUI(datetime: String, abbreviation: String) {
        runOnUiThread {
            binding.dateTime.text = "$datetime $abbreviation"
        }
    }

    fun addProducts(){
        ProjectDB.productsDAO().insertTask(Products(9,"Apple","Food","10.23.2023",10, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(1,"Banana","Food","10.23.2023",24, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(2,"Orange","Food","10.23.2023",20, InCart = false, IsPurchased = false))

        ProjectDB.productsDAO().insertTask(Products(3,"Samsung","Phone","11.23.2023",1000, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(4,"Huawei","Phone","12.23.2023",1200, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(5,"Xiaomi","Phone","13.23.2023",800, InCart = false, IsPurchased = false))

        ProjectDB.productsDAO().insertTask(Products(6,"Nike","Shoes","14.23.2023",250, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(7,"Adidas","Shoes","15.23.2023",200, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(8,"Puma","Shoes","16.23.2023",150, InCart = false, IsPurchased = false))

    }
}
package com.onurakin.project.view

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.room.Room
import com.onurakin.project.databinding.ActivityMainBinding
import com.onurakin.project.db.Products.Products
import com.onurakin.project.db.Products.ProductRoomDatabase
import com.sefikonurakin_hw2.util.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.onurakin.project.adapter.CustomRecyclerViewAdapter
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
class MainActivity : AppCompatActivity(), CustomRecyclerViewAdapter.OnTaskItemClickListener {

    lateinit var binding: ActivityMainBinding
    lateinit var customDialog: Dialog
    lateinit var Date:TextView
    var DeletedTasks = mutableListOf<Products>()
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

        getApi().start()
        addProducts()
        getData()
        fillSpinner()
        binding.recyclerofProducts.setLayoutManager(LinearLayoutManager(this))
        binding.apply {

        }
    }

    fun fillSpinner() {
        val spinner = binding.productTypes

        val productTypes = ProjectDB.productsDAO().getDistinctProductTypes()

        val adapter = CustomRecyclerViewAdapter(this, mutableListOf(), this)
        binding.recyclerofProducts.adapter = adapter

        val initialProducts = ProjectDB.productsDAO().getAllTasks()
        adapter.updateData(initialProducts)

        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productTypes)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        // Set a listener for item selection
        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.d("OnItemSelected", "Selected item at position: $position")
                val selectedProductType = parent?.getItemAtPosition(position).toString()
                Log.d("SelectedCategory", selectedProductType)

                val categoryProducts = ProjectDB.productsDAO().getProductsByCategory(selectedProductType)
                Log.d("CategoryProducts", categoryProducts.toString())

                adapter.updateData(categoryProducts)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

    }



    private fun displayTasks(products: MutableList<Products>) {
        adapter = CustomRecyclerViewAdapter(this, products, this)

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


    override fun onTaskItemClick(task: Products, operation: String) {
        Log.d("item", task.toString())
       // if()
    }
    public fun getApi(): Thread {

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

    // add products to database
    fun addProducts(){
        ProjectDB.productsDAO().insertTask(Products(9,"Apple","Food","10.23.2023",10, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(1,"Banana","Cem","1999",24, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(2,"Orange","Food","10.23.2023",20, InCart = false, IsPurchased = false))

        ProjectDB.productsDAO().insertTask(Products(3,"Samsung","Phone","11.23.2023",1000, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(4,"Huawei","Phone","12.23.2023",1200, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(5,"Xiaomi","Phone","13.23.2023",800, InCart = false, IsPurchased = false))

        ProjectDB.productsDAO().insertTask(Products(6,"Nike","Shoes","14.23.2023",250, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(7,"Adidas","Shoes","15.23.2023",200, InCart = false, IsPurchased = false))
        ProjectDB.productsDAO().insertTask(Products(8,"Puma","Shoes","16.23.2023",150, InCart = false, IsPurchased = false))

    }
}
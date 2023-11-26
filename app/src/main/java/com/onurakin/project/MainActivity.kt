package com.onurakin.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.onurakin.project.ApiServices.ExternalApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.text.toInt
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var editname: EditText
    lateinit var editprice: EditText
    lateinit var btnadd: Button
    lateinit var btnview: Button
    lateinit var date:TextView

    lateinit var url:String
    private lateinit var sqLitehelper: SQLitehelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        getApi().start()



        sqLitehelper = SQLitehelper(this)

        editname = findViewById(R.id.product_name)
        editprice = findViewById(R.id.product_price)
        btnadd = findViewById(R.id.button_add)
        btnview = findViewById(R.id.button_view)
        date = findViewById(R.id.date_time)

        btnadd.setOnClickListener {
            addProduct()
        }
        btnview.setOnClickListener {
            getProducts()
        }


    }

    private fun getProducts() {
        val products = sqLitehelper.getProducts()
        for (product in products) {
            Toast.makeText(this, product.name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addProduct() {
        val name = editname.text.toString()
        val price = editprice.text.toString()

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please enter all the data", Toast.LENGTH_SHORT).show()
        } else {
            val product = Product()

            product.name = name
            product.price = price.toInt()
            Log.d("inserted", "item is : ${product.id} ${product.name } ${product.price} ")
            val status = sqLitehelper.insertProduct(product)
            if (status > -1) {
                Toast.makeText(this, "Product is added", Toast.LENGTH_SHORT).show()
                editname.text.clear()
                editprice.text.clear()
            } else {
                val errorText = "Error: $status"
                Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getApi(): Thread {

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
            date.text = "$datetime $abbreviation"
        }
    }



}
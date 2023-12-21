package com.onurakin.project.view

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.onurakin.project.R
import com.onurakin.project.databinding.ActivityLoginScreenBinding
import com.onurakin.project.db.Products.ProductRoomDatabase
import com.onurakin.project.db.Users.UserRoomDatabase
import com.onurakin.project.db.Users.Users
import com.sefikonurakin_hw2.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class LoginScreen : AppCompatActivity() {

    lateinit var binding: ActivityLoginScreenBinding
    var isRegisterMode: Boolean = false

    private val ProjectDB: ProductRoomDatabase by lazy {
        Room.databaseBuilder(this, ProductRoomDatabase::class.java, Constants.DATABASENAME)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.genderradiogroup.visibility = View.GONE

        binding.OKButton.setOnClickListener {
            val username = binding.Username.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            if (validateInput(username, password)) {
                if (isRegisterMode) {
                    // Call register function
                    registerUser(username, password)
                } else {
                    // Call login function
                    loginUser(username, password)
                }
            } else {
                Toast.makeText(this, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }

        binding.Login.setOnClickListener {
            // Ok button pressed, hide genderradiogroup
            binding.genderradiogroup.visibility = View.GONE
        }

        binding.Register.setOnClickListener {
            // Register button pressed, show genderradiogroup
            binding.genderradiogroup.visibility = View.VISIBLE
            isRegisterMode = true
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        return username.isNotEmpty() && username.length > 6 &&
                password.isNotEmpty() && password.length > 6
    }

    private fun loginUser(username: String, password: String) {
        val userList = ProjectDB.usersDAO().getUsersByName(username)

        if (userList.isNotEmpty()) {
            val user = userList[0]

            if (user.Password == password) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "wrong password", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
        }
    }


    private suspend fun getApi(): String {
        return withContext(Dispatchers.IO) {
            val url = URL("https://worldtimeapi.org/api/timezone/Europe/Istanbul")
            val connection = url.openConnection() as HttpURLConnection

            if (connection.responseCode == 200) {
                val apiResponse = connection.inputStream
                val inputStreamReader = InputStreamReader(apiResponse, "UTF-8")
                val request = Gson().fromJson(inputStreamReader, JsonObject::class.java)
                val datetime = request.get("datetime").toString()
                inputStreamReader.close()
                apiResponse.close()
                return@withContext datetime
            } else {
                Log.d("api", "Error: ${connection.responseCode}")
                return@withContext ""
            }
        }
    }

    private fun registerUser(username: String, password: String) {
        val selectedGenderId = binding.genderradiogroup.checkedRadioButtonId
        val gender = when (selectedGenderId) {
            R.id.Male -> "Male"
            R.id.Female -> "Female"
            else -> ""
        }

        val datetime = runBlocking {
            getApi()
        }
        Log.d("UserRegistration", "User registered - Username: $username, Password: $password))")
        Log.d("UserRegistration", "gender: $gender, datetime: $datetime")

        val newUser = Users(
            UserName = username,
            Gender = gender,
            JoinDate = datetime,
            Money = 10000,
            Password = password
        )

        ProjectDB.usersDAO().insertUser(newUser)
        Log.d("UserRegistration", "User inserted into database")

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}

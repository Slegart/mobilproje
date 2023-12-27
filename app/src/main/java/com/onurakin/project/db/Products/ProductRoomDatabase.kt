package com.onurakin.project.db.Products
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.onurakin.project.db.Users.UsersDAO
import com.onurakin.project.db.Users.Users
import com.sefikonurakin_hw2.util.Constants

@Database(entities = [Products::class,Users::class], version = 5)
abstract class ProductRoomDatabase : RoomDatabase() {
    abstract fun productsDAO(): ProductsDAO
    abstract fun usersDAO(): UsersDAO


}

package com.onurakin.project.db.Products
import androidx.room.Database
import androidx.room.RoomDatabase
import com.onurakin.project.db.Users.UsersDAO

//If you change anything on the database like adding a field to table, chnaging type of a filed, deleting a filed, changing the name of the field
@Database(entities = [Products::class], version = 10)
abstract class ProductRoomDatabase : RoomDatabase() {
    abstract fun productsDAO(): ProductsDAO
    //abstract fun usersDAO(): UsersDAO
}

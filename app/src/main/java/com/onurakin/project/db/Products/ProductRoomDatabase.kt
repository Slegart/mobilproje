package com.onurakin.project.db.Products.Products
import androidx.room.Database
import androidx.room.RoomDatabase
import com.onurakin.project.db.Products.Products.Products
import com.onurakin.project.db.Products.Products.ProductsDAO
import com.onurakin.project.db.Products.User.UsersDAO

//If you change anything on the database like adding a field to table, chnaging type of a filed, deleting a filed, changing the name of the field
@Database(entities = [Products::class], version = 9)
abstract class ProductRoomDatabase : RoomDatabase() {
    abstract fun productsDAO(): ProductsDAO
   // abstract fun usersDAO(): UsersDAO
 

}

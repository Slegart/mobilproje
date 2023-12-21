package com.onurakin.project.db.Users
import androidx.room.Database
import androidx.room.RoomDatabase
import com.onurakin.project.db.Users.UsersDAO

//If you change anything on the database like adding a field to table, chnaging type of a filed, deleting a filed, changing the name of the field
@Database(entities = [Users::class], version = 2)
abstract class UserRoomDatabase : RoomDatabase() {

    //abstract fun usersDAO(): UsersDAO

}
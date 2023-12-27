package com.onurakin.project.db.Users
import androidx.room.Database
import androidx.room.RoomDatabase
import com.onurakin.project.db.Users.UsersDAO

@Database(entities = [Users::class], version = 2)
abstract class UserRoomDatabase : RoomDatabase() {

    //abstract fun usersDAO(): UsersDAO

}
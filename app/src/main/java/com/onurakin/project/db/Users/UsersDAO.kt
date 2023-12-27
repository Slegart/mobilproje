package com.onurakin.project.db.Users
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sefikonurakin_hw2.util.Constants


@Dao
interface UsersDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(users: Users)

    @Update
    fun updateUser(users: Users)

    @Delete
    fun deleteUser(users: Users)

    @Query("DELETE FROM ${Constants.TABLEUSERS}")
    fun deleteAllUsers()

    @Query("SELECT * FROM ${Constants.TABLEUSERS} ORDER BY id DESC")
    fun getAllUsers():MutableList<Users>

    @Query("SELECT * FROM ${Constants.TABLEUSERS} WHERE id =:id")
    fun getUserById(id:Int): Users

    @Query("SELECT * FROM ${Constants.TABLEUSERS} WHERE UserName LIKE :name")
    fun getUsersByName(name:String):MutableList<Users>

    @Query("UPDATE ${Constants.TABLEUSERS} SET Money = :money WHERE id = :id")
    fun updateMoney(id:Int, money:Int)

}
package com.onurakin.project.db.Products
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sefikonurakin_hw2.util.Constants


@Dao
interface ProductsDAO {
    // The conflict strategy defines what happens,if there is an existing entry.
    // The default action is ABORT.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(tasks: Products)

    @Update
    fun updateTask(tasks: Products)

    @Delete
    fun deleteTask(tasks: Products)

    @Query("DELETE FROM ${Constants.TABLENAME}")
    fun deleteAllTasks()

    @Query("SELECT * FROM ${Constants.TABLENAME} ORDER BY id DESC")
    fun getAllTasks():MutableList<Products>

    @Query("SELECT * FROM ${Constants.TABLENAME} WHERE id =:id")
    fun getTaskById(id:Int): Products

    @Query("SELECT * FROM ${Constants.TABLENAME} WHERE ProductName LIKE :name")
    fun getTasksByName(name:String):MutableList<Products>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTasks(tasks: ArrayList<Products>){
        tasks.forEach{
            insertTask(it)
        }
    }
    @Query("SELECT DISTINCT productType FROM ${Constants.TABLENAME}")
    fun getDistinctProductTypes(): List<String>

    @Query("SELECT * FROM Products WHERE ProductType = :ProductType")
    fun getProductsByCategory(ProductType: String): List<Products>

}
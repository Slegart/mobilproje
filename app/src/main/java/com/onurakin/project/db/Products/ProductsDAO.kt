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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(tasks: Products)

    @Update
    fun updateTask(tasks: Products)

    @Query("UPDATE ${Constants.TABLEPRODUCTS} SET inCart = :inCartValue WHERE id = :productId")
    fun updateInCartStatus(productId: Int, inCartValue: Boolean)

    @Delete
    fun deleteTask(tasks: Products)

    @Query("DELETE FROM ${Constants.TABLEPRODUCTS}")
    fun deleteAllTasks()

    @Query("SELECT * FROM ${Constants.TABLEPRODUCTS} ORDER BY id DESC")
    fun getAllTasks():MutableList<Products>

    @Query("DELETE FROM ${Constants.TABLEPRODUCTS} WHERE id = :id")
    fun deleteTaskById(id: Int)

    @Query("SELECT * FROM ${Constants.TABLEPRODUCTS} WHERE id =:id")
    fun getTaskById(id:Int): Products

    @Query("SELECT * FROM ${Constants.TABLEPRODUCTS} WHERE ProductName LIKE :name")
    fun getTasksByName(name:String):MutableList<Products>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllTasks(tasks: ArrayList<Products>){
        tasks.forEach{
            insertTask(it)
        }
    }
    @Query("SELECT DISTINCT productType FROM ${Constants.TABLEPRODUCTS}")
    fun getDistinctProductTypes(): List<String>

    @Query("SELECT * FROM Products WHERE ProductType = :ProductType")
    fun getProductsByCategory(ProductType: String): List<Products>

}
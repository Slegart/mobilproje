package com.onurakin.project

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLitehelper(context: Context) : SQLiteOpenHelper(context, "database", null, 1) {

    companion object {
        const val DATABASE_NAME = "database"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "Products"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_NAME VARCHAR(256), $COLUMN_PRICE INTEGER)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    fun insertProduct(product: Product): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, product.name)
        contentValues.put(COLUMN_PRICE, product.price)
        val status = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return status
    }



    @SuppressLint("Range")
    fun getProducts(): ArrayList<Product> {
        val productList = ArrayList<Product>()
        val selectquery = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectquery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL("DROP TABLE $TABLE_NAME")
            onCreate(db)
            return ArrayList()
        }
        var id: Int
        var name: String
        var price: Int
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                name = cursor.getString(cursor.getColumnIndex("name"))
                price = cursor.getInt(cursor.getColumnIndex("price"))
                val product = Product()
                product.id = id
                product.name = name
                product.price = price
                productList.add(product)
            } while (cursor.moveToNext())
        }
        return productList
    }

}
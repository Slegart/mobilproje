package com.onurakin.project.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.util.Log
import com.onurakin.project.db.Products.ProductRoomDatabase

class WorkerClass(
    appContext: Context,
    workerParams: WorkerParameters,
    private val database: ProductRoomDatabase
) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val productIds = inputData.getIntArray("productIds")
        Log.d("WorkerClass", "Product IDs: ${productIds?.contentToString()}")

        if (productIds != null) {
            val products = database.productsDAO().getAllTasks()

            Log.d("WorkerClass", "All Products: $products")

            for (productIdToCheck in productIds) {
                val isProductInList = products.any { it.id == productIdToCheck }

                if (isProductInList) {
                    Log.d("WorkerClass", "Product with ID $productIdToCheck is in the list.")
                    database.productsDAO().deleteTaskById(productIdToCheck)
                    Log.d("WorkerClass", "Product with ID $productIdToCheck is deleted.")
                } else {
                    Log.d("WorkerClass", "Product with ID $productIdToCheck is not in the list.")
                }
            }
        }

        val products = database.productsDAO().getAllTasks()
        Log.d("WorkerClass", "All Products left: $products")

        return Result.success()
    }


}

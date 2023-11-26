package com.onurakin.project

class Product {

    var id: Int = getautoid()
    var name: String? = null
    var price: Int = 0

    companion object{
        fun getautoid(): Int {
            val random = (0..100).random()
            return random
        }
    }
}
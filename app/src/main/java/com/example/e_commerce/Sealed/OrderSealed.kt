package com.example.e_commerce.Sealed

import com.example.e_commerce.Model.Order

sealed class OrderSealed {

    class Success(val data :MutableList<Order>):OrderSealed()
    class Failure(val message:String):OrderSealed()
    object Loading :OrderSealed()

}
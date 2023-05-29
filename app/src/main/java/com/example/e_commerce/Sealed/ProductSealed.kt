package com.example.e_commerce.Sealed

import com.example.e_commerce.Model.Product

sealed class ProductSealed {

    class Success(val data :MutableList<Product>):ProductSealed()
    class Failure(val message:String):ProductSealed()
    object Loading :ProductSealed()

}
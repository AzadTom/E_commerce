package com.example.e_commerce.Sealed

import com.example.e_commerce.Model.Cartitem

sealed class Cartsealed {

    class Success(val data :MutableList<Cartitem>):Cartsealed()
    class Failure(val message:String):Cartsealed()
    object Loading :Cartsealed()

}
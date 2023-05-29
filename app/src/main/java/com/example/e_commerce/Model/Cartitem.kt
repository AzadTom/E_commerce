package com.example.e_commerce.Model

data class Cartitem(
    var id:String ="",
    var productname:String="",
    var subdescription:String="",
    var description:String="",
    var imageUrl:String="",
    var price:Int =0,
    var quantity:Int =1
)

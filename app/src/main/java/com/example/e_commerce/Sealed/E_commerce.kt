package com.example.e_commerce.Sealed

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class E_commerce(val route:String,val label:String,val icons:ImageVector){

    object Home : E_commerce("Home","Home", Icons.Rounded.CatchingPokemon)
    object Order: E_commerce("Order","Order",Icons.Rounded.ShoppingBag)
    object Cart: E_commerce("favourite","favourite",Icons.Rounded.Favorite)
    object Account: E_commerce("Profile","Profile",Icons.Rounded.Person)



}

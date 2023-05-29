package com.example.e_commerce.Screen.Order

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SuccessScreen(){


    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


       Column {

           Image(imageVector = Icons.Default.Done , contentDescription = null)
           Spacer(modifier = Modifier.height(8.dp))
           Text(text = "Successful", fontSize = 16.sp, color = Color.DarkGray)
       }


    }


}
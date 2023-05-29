package com.example.e_commerce.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.e_commerce.Model.Order
import com.example.e_commerce.Sealed.OrderSealed
import com.example.e_commerce.ViewModel.ProductViewmodel

@Composable
fun OrderScreen(productViewmodel: ProductViewmodel) {


    when (val result = productViewmodel.readorder.value) {

        is OrderSealed.Loading -> {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


                CircularProgressIndicator()

            }

        }

        is OrderSealed.Success -> {


            if (result.data.isNotEmpty()) {
                Order(productViewmodel, result.data)
            } else {

                Scaffold(
                    topBar = { OrderTopBar() }, modifier = Modifier.background(MaterialTheme.colors.primary)
                        .padding(16.dp)
                ) {

                    Box(
                        Modifier.fillMaxSize().background(MaterialTheme.colors.primary).padding(it),
                        contentAlignment = Alignment.Center
                    ) {

                        Text(text = "There is no Order!", fontSize = 18.sp, color = MaterialTheme.colors.primaryVariant)
                    }
                }

            }


        }
        else -> {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


                Text(text = "Error Fetching data!")


            }

        }
    }


}

@Composable
fun Order(productViewmodel: ProductViewmodel, data: MutableList<Order>) {


    Scaffold(
        topBar = { OrderTopBar() }, modifier = Modifier
            .fillMaxSize().background(MaterialTheme.colors.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.primary).padding(it)
        )
        {

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(data) {

                OrderItem(order = it, productViewmodel = productViewmodel)


            }

        }
    }


}

@Composable
fun OrderTopBar() {

    Row(
        Modifier
            .fillMaxWidth().background(MaterialTheme.colors.primary).padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        Text(
            text = "Order",
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = MaterialTheme.colors.primaryVariant
        )


    }
}


@Composable
fun OrderItem(order: Order, productViewmodel: ProductViewmodel) {

    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.secondary)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = order.productname,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$${order.price} * ${order.quantity} = $${order.price * order.quantity}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = { productViewmodel.removeToOrder(order) },
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colors.primaryVariant
                    )
                ) {

                    Text(text = "Remove", color = MaterialTheme.colors.primary)

                }

                Text(
                    text = order.date.uppercase(),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
                Spacer(modifier = Modifier.height(4.dp))


            }


            Spacer(modifier = Modifier.width(8.dp))

            AsyncImage(
                model = order.imageUrl.toUri(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(16.dp))

            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
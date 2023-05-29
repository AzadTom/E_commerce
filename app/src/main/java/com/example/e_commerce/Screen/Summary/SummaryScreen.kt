package com.example.e_commerce.Screen.Summary

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.e_commerce.Model.Cartitem
import com.example.e_commerce.Model.Order
import com.example.e_commerce.Screen.Counter
import com.example.e_commerce.Screen.iscartcontainslist
import com.example.e_commerce.Sealed.Cartsealed
import com.example.e_commerce.ViewModel.ProductViewmodel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SummaryScreen(productViewmodel: ProductViewmodel, onback: () -> Unit, onSucess: () -> Unit) {


    when (val result = productViewmodel.readcard.value) {

        is Cartsealed.Loading -> {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


                CircularProgressIndicator()

            }

        }

        is Cartsealed.Success -> {


            Summary({ onback() }, result.data, onSucess, productViewmodel)


        }
        else -> {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


                Text(text = "Error Fetching data!")


            }

        }
    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Summary(
    onback: () -> Unit,
    data: MutableList<Cartitem>,
    onSucess: () -> Unit,
    productViewmodel: ProductViewmodel
) {

    Scaffold(topBar = { SummaryTopBar(onback = { onback() }) }) {

        LazyColumn(
            Modifier
                .fillMaxSize().background(MaterialTheme.colors.primary)
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        {

            items(data)
            {

                SumaryTextItem(cartitem = it)


            }

            item {

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Details",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.primaryVariant
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            item {

                LazyRow(modifier = Modifier.fillMaxWidth()) {

                    items(data) {

                        SummaryItem(cartitem = it)

                    }
                }
            }

            item {


                Column(
                    modifier = Modifier.fillMaxSize(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    SummaryPrice(data, onSucess, productViewmodel = productViewmodel)
                }

            }

        }

    }


}

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SummaryPrice(
    data: MutableList<Cartitem>,
    onSucess: () -> Unit,
    productViewmodel: ProductViewmodel
) {


    var total = 0
    data.forEach {

        total += it.price * it.quantity

    }



    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {


            Text(
                text = "Subtotal",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primaryVariant
            )

            Text(
                text = "$${total}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color =  MaterialTheme.colors.primaryVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {


            Text(
                text = "ShippingCharge",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primaryVariant
            )

            Text(
                text = "$${2000}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primaryVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {


            Text(
                text = "Total",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primaryVariant
            )

            Text(
                text = "$${total + 2000}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colors.primaryVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

            Button(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp), shape = CircleShape,
                onClick = {

                    val sdf = SimpleDateFormat("dd/M/yyyy hh:mm a")
                    val current = sdf.format(Date())

                    data.forEach {

                        val order = Order(
                            id = it.id,
                            productname = it.productname,
                            subdescription = it.subdescription,
                            description = it.description,
                            imageUrl = it.imageUrl,
                            it.price,
                            quantity = it.quantity,
                            date = current.toString()
                        )

                        productViewmodel.addToOrder(order)


                    }
                    data.forEach {

                        productViewmodel.removetocart(cartitem = it)
                    }

                    onSucess()

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colors.primaryVariant
                )
            ) {

                Text(text = "Place order", color = MaterialTheme.colors.primary)
            }
        }
    }
}


@Composable
fun SummaryTopBar(onback: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth().background(MaterialTheme.colors.primary),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .clickable { onback() },
            tint = MaterialTheme.colors.primaryVariant
        )
        Text(
            text = "Summary",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = MaterialTheme.colors.primaryVariant
        )


    }
}

@Composable
fun SummaryItem(cartitem: Cartitem) {

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
                    text = cartitem.productname,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$${cartitem.price} * ${cartitem.quantity} = $${cartitem.price * cartitem.quantity}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                            color = MaterialTheme.colors.primaryVariant
                )
                Spacer(modifier = Modifier.height(4.dp))


            }


            Spacer(modifier = Modifier.width(8.dp))

            AsyncImage(
                model = cartitem.imageUrl.toUri(),
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


@Composable
fun SumaryTextItem(cartitem: Cartitem) {

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.secondary)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {

        Text(text = cartitem.productname, fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = MaterialTheme.colors.primaryVariant)
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "$${cartitem.price} * ${cartitem.quantity} = $${cartitem.price * cartitem.quantity}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colors.primaryVariant
        )
        Spacer(modifier = Modifier.height(4.dp))


    }

}


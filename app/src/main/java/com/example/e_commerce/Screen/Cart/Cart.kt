package com.example.e_commerce.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.e_commerce.Model.Cartitem
import com.example.e_commerce.Model.Product
import com.example.e_commerce.Sealed.Cartsealed
import com.example.e_commerce.ViewModel.ProductViewmodel

@Composable
fun Cart(productViewmodel: ProductViewmodel,onback:()->Unit,onnext:(Product)->Unit,onsummary: () -> Unit) {

    when (val result = productViewmodel.readcard.value) {

        is Cartsealed.Loading -> {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


                CircularProgressIndicator()

            }


        }

        is Cartsealed.Success -> {


            if (result.data.isNotEmpty()){

                UI(
                    productViewmodel = productViewmodel,
                    onnext = onnext,
                    onback = { onback() },
                    cartlist = result.data,
                    onsummary = { onsummary() }
                )

            }
            else
            {


                Scaffold(topBar = { CartTopBar {
                    onback()
                }}) {

                    Column(
                        Modifier
                            .fillMaxSize().background(MaterialTheme.colors.primary)
                            .padding(it),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "Cart is Empty!",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colors.primaryVariant
                        )

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
fun UI(
    productViewmodel: ProductViewmodel,
    onnext: (Product) -> Unit,
    onback: () -> Unit,
    cartlist: MutableList<Cartitem>,
    onsummary: () -> Unit
) {

    var total = 0
    cartlist.forEach {

        total += it.price*it.quantity

    }

   
    Scaffold(topBar = {CartTopBar { onback() } }, bottomBar = {CartBottombar(cartlist,total,onsummary)}) {

        LazyColumn(
            Modifier
                .fillMaxSize().background(MaterialTheme.colors.primary)
                .padding(it)
                .padding(16.dp)) {


            items(cartlist)
            {
                    val product = Product(it.id,it.productname,it.subdescription,it.description,it.imageUrl,it.price)

                    CartItem(it,productViewmodel,{onnext(product)})


            }


        }
    }

}

@Composable
fun CartBottombar(cartlist: MutableList<Cartitem>,total:Int,onsummary: () -> Unit) {



    if (cartlist.isEmpty())
    {
        Unit
    }
    else{


        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)) {

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
                    color = MaterialTheme.colors.primaryVariant
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
                    text = "$${total+2000}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colors.primaryVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {



                Button(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp), shape = CircleShape,onClick = { onsummary()},  modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colors.primaryVariant)) {

                    Text(text = "Checkout", color = MaterialTheme.colors.primary)
                }
            }
        }
    }






}



@Composable
fun CartTopBar(back: () -> Unit) {


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
                .clickable { back() },
            tint = MaterialTheme.colors.primaryVariant
        )
        Text(
            text = "Cart",
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp,
            color = MaterialTheme.colors.primaryVariant
        )


    }
}


@Composable
fun CartItem(cartitem: Cartitem, productViewmodel: ProductViewmodel, onnext: () -> Unit) {



    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onnext() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.secondary)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(text = "${cartitem.productname}", fontWeight = FontWeight.SemiBold, fontSize = 18.sp, color = MaterialTheme.colors.primaryVariant)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = "$${cartitem.price}", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = MaterialTheme.colors.primaryVariant)
                Spacer(modifier = Modifier.height(4.dp))
                Counter(productViewmodel,cartitem)

            }

            AsyncImage(
                model = cartitem.imageUrl.toUri(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(125.dp)
                    .height(125.dp)
                    .clip(RoundedCornerShape(16.dp))

            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }

}

@Composable
fun Counter(productViewmodel: ProductViewmodel, cartitem: Cartitem) {



    Row(
        modifier = Modifier.fillMaxWidth(.4f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(tint=MaterialTheme.colors.primaryVariant,
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .padding(8.dp)
                .clickable { productViewmodel.addtocart(cartitem.copy(quantity = cartitem.quantity + 1)) }
        )

        Text("${cartitem.quantity}", color = MaterialTheme.colors.primaryVariant)

        Icon(tint =MaterialTheme.colors.primaryVariant,
            imageVector = Icons.Default.Remove,
            contentDescription = null,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .padding(8.dp)
                .clickable {

                    if (cartitem.quantity > 1) {

                        productViewmodel.addtocart(cartitem.copy(quantity = cartitem.quantity - 1))
                    } else {
                        productViewmodel.removetocart(cartitem)
                    }


                }
        )

    }


}


package com.example.e_commerce.Screen

import TextSeacrhBar
import android.net.Uri
import android.widget.Space
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.e_commerce.Model.Product
import com.example.e_commerce.Sealed.Cartsealed
import com.example.e_commerce.Sealed.ProductSealed
import com.example.e_commerce.ViewModel.ProductViewmodel

@Composable
fun Home(productViewmodel: ProductViewmodel, onclick: (Product) -> Unit, oncart: () -> Unit,isthem:MutableState<Boolean>,onnext: () -> Unit) {


    when (val result = productViewmodel.response.value) {

        is ProductSealed.Loading -> {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {


                CircularProgressIndicator(color = MaterialTheme.colors.secondary)

            }


        }

        is ProductSealed.Success -> {


            when (val favresponse = productViewmodel.favresponse.value) {

                is ProductSealed.Loading -> {

                    CircularProgressIndicator()
                }

                is ProductSealed.Success -> {


                    when (val cartdata = productViewmodel.readcard.value) {

                        is Cartsealed.Loading -> {

                        }

                        is Cartsealed.Success -> {




                            LazyHome(
                                productlist = result.data,
                                onclick = { onclick(it) },
                                favouritelist = favresponse.data,
                                cartdata.data,
                                productViewmodel = productViewmodel,
                                oncart = {oncart()},isthem, onnext = {onnext()}
                            )

                        }
                        else -> {


                        }
                    }

                }
                else -> {

                    Text(text = "Error Fetching data!")
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
fun LazyHome(
    productlist: MutableList<Product>, onclick: (Product) -> Unit,
    favouritelist: MutableList<Product>,
    cartlist: MutableList<Cartitem>,
    productViewmodel: ProductViewmodel,
    oncart: () -> Unit,
    isthem: MutableState<Boolean>,
    onnext:()->Unit
) {









    Scaffold(
        topBar = { AppBar({ oncart() },isthem) }, modifier = Modifier
            .background(MaterialTheme.colors.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) { paddingvalue ->


        LazyColumn(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.primary)
                .padding(paddingvalue)
        ) {




            item {

                Spacer(modifier = Modifier.height(12.dp))
                TextSeacrhBar({onnext()})



            }









            item {

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Popular Product for you",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.primaryVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ){

                    items(productlist.take(7))
                    {product->


                        TopProduct(product){

                            onclick(it)
                        }





                    }


                }

            }





            item {

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Top Product for you",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.primaryVariant
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            items(productlist.windowed(2, 2, true)) { productlist ->


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {


                    productlist.forEach { product ->

                        val isFav: Boolean = favouritelist.contains(product)
                        val iscart :Boolean = iscartcontainslist(cartlist,product)

                        SimilarHomeProduct(product = product, isFav,iscart, productViewmodel) {

                            onclick(product)
                        }

                    }


                }


            }

        }


    }

}

@Composable
fun TopProduct(product: Product,onclick: (Product) -> Unit) {


    Column(Modifier.fillMaxWidth().padding(end = 16.dp, top = 8.dp, bottom = 8.dp).clickable { onclick(product) }) {

        AsyncImage(
            model = product.imageUrl.toUri(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(300.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)))


    }


}

fun iscartcontainslist(cartlist: MutableList<Cartitem>, product: Product): Boolean {


    for (item in cartlist) {

        if (item.id == product.id) {
            return true
        }


    }

    return false

}


@Composable
fun Item(
    productViewmodel: ProductViewmodel,
    product: Product,
    onclick: () -> Unit,
    isFavourite: Boolean,
    iscart: Boolean
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .height(250.dp)
            .padding(16.dp)
            .clickable { onclick() }
    ) {


        ItemImageHome(
            productViewmodel,
            product.imageUrl.toUri(),
            isFavourite = isFavourite,
            product
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Text(
                text = "$${product.price}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Icon(
                imageVector = if (iscart) Icons.Default.AddShoppingCart else Icons.Default.RemoveShoppingCart,
                contentDescription = null,
                modifier = Modifier.clickable {

                    val cartitem =
                        Cartitem(
                            product.id,
                            product.productname,
                            product.subdescription,
                            product.description,
                            product.imageUrl,
                            product.price,0)


                    if (iscart) {
                        productViewmodel.removetocart(cartitem)
                    }
                    else
                    {
                        productViewmodel.addtocart(cartitem)

                    }
                })


        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.productname,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray
        )
    }


}

@Composable
fun ItemImageHome(
    productViewmodel: ProductViewmodel,
    imageuri: Uri,
    isFavourite: Boolean = false,
    product: Product
) {

    Box(contentAlignment = Alignment.TopStart) {
        AsyncImage(
            model = imageuri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))

        )

        Icon(
            imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(52.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.primary)
                .padding(16.dp)
                .clickable {
                    if (isFavourite) productViewmodel.removeFav(product) else productViewmodel.favadd(
                        product
                    )
                }, tint = MaterialTheme.colors.primaryVariant)

    }
}


@Composable
fun SimilarHomeProduct(
    product: Product,
    isFav: Boolean,
    iscart: Boolean,
    productViewmodel: ProductViewmodel,
    onclick: () -> Unit
) {


    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(vertical = 16.dp)
            .clickable { onclick() }

    ) {

        ItemImageHome(
            productViewmodel = productViewmodel,
            imageuri = product.imageUrl.toUri(),
            product = product,
            isFavourite = isFav
        )



        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {

            Text(
                text = "$${product.price}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colors.primaryVariant
            )

            Icon(
                tint=MaterialTheme.colors.primaryVariant,
                imageVector = if (iscart) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                contentDescription = null,
                modifier = Modifier.clickable {

                    val cartitem =
                        Cartitem(
                            product.id,
                            product.productname,
                            product.subdescription,
                            product.description,
                            product.imageUrl,
                            product.price,1)


                    if (iscart) {
                        productViewmodel.removetocart(cartitem)
                    }
                    else
                    {
                        productViewmodel.addtocart(cartitem)

                    }
                })


        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.productname,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colors.onPrimary
        )
    }


}


@Composable
fun ItemImage(imageuri: Uri) {


    val isFavourite = remember {
        mutableStateOf(false)
    }
    Box(contentAlignment = Alignment.TopStart) {
        AsyncImage(
            model = imageuri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(250.dp)
                .clip(RoundedCornerShape(16.dp))

        )

        Icon(
            imageVector = if (isFavourite.value) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(52.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .padding(16.dp)
                .clickable { isFavourite.value = !isFavourite.value })

    }
}

@Composable
fun AppBar(oncart: () -> Unit, isthem: MutableState<Boolean>) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {



        Row(  verticalAlignment = Alignment.CenterVertically){


            Icon(
                imageVector =  Icons.Default.Light,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp, bottom = 8.dp, top = 8.dp)
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primaryVariant)
                    .padding(16.dp)
                    .clickable {

                        isthem.value = !isthem.value

                    }, tint = MaterialTheme.colors.primary)

            Text(text = "Discover", fontSize = 24.sp, color = MaterialTheme.colors.primaryVariant)
        }


        Icon(
            imageVector = Icons.Default.ShoppingBag,
            contentDescription = null,
            tint= MaterialTheme.colors.primaryVariant,
            modifier = Modifier.clickable { oncart() })

    }

}

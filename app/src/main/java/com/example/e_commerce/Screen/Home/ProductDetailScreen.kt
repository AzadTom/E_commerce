package com.example.e_commerce.Screen.Home

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingBag
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
import com.example.e_commerce.Screen.ItemImage
import com.example.e_commerce.Screen.ItemImageHome
import com.example.e_commerce.Screen.SimilarHomeProduct
import com.example.e_commerce.Screen.iscartcontainslist
import com.example.e_commerce.Sealed.Cartsealed
import com.example.e_commerce.Sealed.ProductSealed
import com.example.e_commerce.ViewModel.ProductViewmodel


@Composable
fun ProductDetailScreen(productViewmodel: ProductViewmodel, onback :()->Unit,onnext: () -> Unit,onclick: (Product) -> Unit) {




    when (val result = productViewmodel.response.value) {

        is ProductSealed.Loading -> {

            Box(modifier = Modifier.fillMaxSize()) {


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


                                    NestedScrollbarProductDetail(data = result.data , onback = {onback()}, onnext = {onnext()}, productViewmodel = productViewmodel, favlist = favresponse.data, onclick = {onclick(it)}, cartlist = cartdata.data)


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

        else->{





        }



    }







}












@Composable
fun NestedScrollbarProductDetail(
    data: MutableList<Product>,
    onclick: (Product) -> Unit,
    productViewmodel: ProductViewmodel,
    favlist: MutableList<Product>,
    onback: () -> Unit,
    onnext:()->Unit,
    cartlist:MutableList<Cartitem>
) {

    Scaffold(modifier = Modifier
        .fillMaxSize().background(MaterialTheme.colors.primary)
        .padding(horizontal = 16.dp, vertical = 8.dp), topBar = { ProductDetailAppBar(onback,onnext) }, bottomBar = {}) { paddingValues ->


        LazyColumn(
            Modifier
                .fillMaxSize().background(MaterialTheme.colors.primary)
                .padding(paddingValues = paddingValues)
        ) {



               item {



                   ProductDetail(productViewmodel = productViewmodel,favlist,cartlist,onnext)
               }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "SimilarProduct",
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.primaryVariant,
                    fontWeight = FontWeight.SemiBold
                )


            }


              items(data.windowed(2,2,true)){ productlist->


                      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {


                          productlist.forEach {product ->

                              if (product != productViewmodel.productitem.value)
                              {
                                  val isFav: Boolean = favlist.contains(product)
                                  val iscart:Boolean = iscartcontainslist(cartlist,product)

                                  SimilarHomeProduct(
                                      product = product,
                                      isFav,
                                      iscart,
                                      productViewmodel
                                  ) {

                                      onclick(product)
                                  }
                              }

                          }



                      }




              }


            }


        }
}


@Composable
fun ProductDetail(
    productViewmodel: ProductViewmodel,
    favlist: MutableList<Product>,
    cartlist: MutableList<Cartitem>,
    oncart: () -> Unit
) {



    val product = productViewmodel.productitem.value
    val isFav = favlist.contains(product)
    val iscart = iscartcontainslist(cartlist,product!!)

    Column(
        Modifier
            .fillMaxSize()
    ) {




        Spacer(modifier = Modifier.height(12.dp))

        ItemImageProductDetail(productViewmodel = productViewmodel, imageuri = product.imageUrl.toUri(), product = product, isFavourite = isFav)

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = product.productname,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colors.primaryVariant,
            textAlign = TextAlign.Start,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = product.subdescription, color = MaterialTheme.colors.primaryVariant)


        Pricing(product,iscart,productViewmodel, oncart = { oncart() })


    }

}

@Composable
fun ItemImageProductDetail(
    productViewmodel: ProductViewmodel,
    imageuri: Uri,
    product: Product,
    isFavourite: Boolean
)  {

    Box(contentAlignment = Alignment.TopStart) {
        AsyncImage(
            model = imageuri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))

        )

        Icon(tint = MaterialTheme.colors.primaryVariant,
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
                })

    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun Pricing(product: Product, iscart: Boolean,productViewmodel: ProductViewmodel,oncart: () -> Unit) {


    Column(horizontalAlignment = Alignment.CenterHorizontally) {


        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = product.productname,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colors.primaryVariant
            )

            Text(
                text = "$${product.price}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = MaterialTheme.colors.primaryVariant
            )


        }

        Spacer(modifier = Modifier.height(24.dp))

        if (iscart)
        {
            Button(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp), shape = CircleShape,
                onClick = {

                          oncart()


                }, modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primaryVariant)
            ) {
                Text(text = "Gotocart", color = MaterialTheme.colors.primary, textAlign = TextAlign.Center)
            }

        }
        else{




            Button(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp), shape = CircleShape,
                onClick = {

                    val cartitem =
                        Cartitem(
                            product.id,
                            product.productname,
                            product.subdescription,
                            product.description,
                            product.imageUrl,
                            product.price,1)

                          productViewmodel.addtocart(cartitem)


                }, modifier = Modifier
                    .fillMaxWidth().padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primaryVariant)
            ) {
                Text(text = "Addtocart", color = MaterialTheme.colors.primary, textAlign = TextAlign.Center)
            }

        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(shape = CircleShape,
            onClick = {
                val cartitem =
                    Cartitem(
                        product.id,
                        product.productname,
                        product.subdescription,
                        product.description,
                        product.imageUrl,
                        product.price,1)
                      productViewmodel.addtocart(cartitem)

                oncart()
            }, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 16.dp),
            modifier = Modifier
                .fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text(text = "Buy", color = MaterialTheme.colors.primaryVariant, textAlign = TextAlign.Center)
        }
    }
}



@Composable
fun ProductDetailAppBar(onback: () -> Unit, onnext: () -> Unit) {


    Row(
        modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(tint = MaterialTheme.colors.primaryVariant,imageVector = Icons.Rounded.ArrowBack, contentDescription = null, modifier = Modifier.clickable { onback() })

        Icon(tint = MaterialTheme.colors.primaryVariant,imageVector = Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.clickable { onnext() })


    }

}



@Composable
fun SimilarProduct(product: Product,onclick:()->Unit) {


    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(vertical = 16.dp)
            .clickable { onclick() }

    ) {

        ItemImage(imageuri = product.imageUrl.toUri())



        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.SpaceBetween) {

            Text(
                text = "$${product.price}",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )

            Icon(imageVector = Icons.Default.ShoppingBag, contentDescription = null)


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


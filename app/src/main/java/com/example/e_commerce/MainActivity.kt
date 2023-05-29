package com.example.e_commerce

import SearchBar
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.e_commerce.Repositery.ProductRepositery
import com.example.e_commerce.Screen.*
import com.example.e_commerce.Screen.Account.FavouriteScreen
import com.example.e_commerce.Screen.Home.ProductDetailScreen
import com.example.e_commerce.Screen.Order.SuccessScreen
import com.example.e_commerce.Screen.Summary.SummaryScreen
import com.example.e_commerce.Sealed.E_commerce
import com.example.e_commerce.ViewModel.ProductViewmodel
import com.example.e_commerce.ui.theme.EcommerceTheme


class MainActivity : ComponentActivity() {

    private val productViewmodel: ProductViewmodel by viewModels {
        ProductViewmodel.Factory(
            ProductRepositery()


        )
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        setContent {

            val istheme = remember{ mutableStateOf(false) }
            EcommerceTheme(istheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {

                    Navigation(istheme)


                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun NavigationController(
        navController: NavHostController,
        padding: PaddingValues,
        istheme: MutableState<Boolean>
    ) {


        NavHost(
            navController = navController,
            startDestination = E_commerce.Home.route
        ) {

            composable(route = E_commerce.Home.route) {



                Home(productViewmodel, oncart = {

                    navController.navigate("cart")

                }, onclick = {

                    productViewmodel.addProduct(it)
                    navController.navigate("productdetails")
                }, isthem = istheme, onnext = {  navController.navigate("screen")})

            }

            composable(route = E_commerce.Order.route) {
                OrderScreen(productViewmodel = productViewmodel)
            }

            composable(route = "order1") {
                OrderScreen(productViewmodel = productViewmodel)
            }



            composable("screen")
            {
                SearchBar(productViewmodel = productViewmodel, onnext = {

                    productViewmodel.addProduct(it)
                    navController.navigate("productdetails")
                }, ondiscard = {

                    navController.popBackStack()
                })
            }



            composable(route = "cart") {
                Cart(productViewmodel, {
                    navController.popBackStack()
                }, {

                    productViewmodel.addProduct(it)
                    navController.navigate("productdetails")

                }, onsummary = {

                    navController.navigate("summary")

                })
            }

            composable(route = E_commerce.Account.route) {
                Account(
                    profileImage = R.drawable.pictures,
                    navController = navController
                )
            }

            composable("summary")
            {

                SummaryScreen(productViewmodel = productViewmodel, onSucess = {


                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate("order")



                }, onback = {

                    navController.popBackStack()

                })
            }

            composable(route = "productdetails") {

                ProductDetailScreen(productViewmodel, onback = {
                    navController.popBackStack()
                }, onnext = {

                    navController.navigate("cart")

                }) {

                    productViewmodel.addProduct(it)
                    navController.navigate("productdetails")

                }
            }

            composable("success")
            {
                SuccessScreen()
            }

            composable("favourite") {

                productViewmodel.readFavData()

                FavouriteScreen(
                    productViewmodel = productViewmodel,
                    onnext = {
                        productViewmodel.addProduct(it)
                        navController.navigate("productdetails")
                    }
                )

            }
            composable("favourite1") {

                productViewmodel.readFavData()

                FavouriteScreen(
                    productViewmodel = productViewmodel,
                    onnext = {
                        productViewmodel.addProduct(it)
                        navController.navigate("productdetails")
                    }
                )

            }

            composable(route = "publish") {
                PublishScreen(ondiscard = {

                    navController.popBackStack()

                }, onSubmit = {


                    productViewmodel.submitdata(it, onFailure = {

                        Toast.makeText(applicationContext, it, Toast.LENGTH_LONG).show()


                    }, onSuccess = {

                        Toast.makeText(applicationContext, "Sucessfully added!", Toast.LENGTH_LONG)
                            .show()


                    })
                    navController.popBackStack()

                })
            }


        }


    }

    @Composable
    fun BottomNavigationBar(navController: NavController) {

        val items = listOf(
            E_commerce.Home, E_commerce.Order, E_commerce.Cart, E_commerce.Account
        )

        BottomNavigation(
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 8.dp,
        )
        {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            items.forEach {

                BottomNavigationItem(selected = currentRoute == it.route, label = {
                    Text(
                        text = it.label,
                        color = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                    )
                }, icon = {
                    Icon(
                        imageVector = it.icons,
                        contentDescription = null,
                        tint = if (currentRoute == it.route) Color.DarkGray else Color.LightGray
                    )
                },

                    onClick = {

                        if (currentRoute != it.route) {
                            navController.graph.startDestinationRoute?.let {

                                navController.popBackStack(it, true)
                            }
                            navController.navigate(it.route) {
                                launchSingleTop = true

                            }
                        }

                    })
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Navigation(istheme: MutableState<Boolean>) {

        val navController = rememberNavController()


        Scaffold(bottomBar = {

            if (navController.currentBackStackEntryAsState().value?.destination?.route == "publish" ||
                navController.currentBackStackEntryAsState().value?.destination?.route == "screen" ||
                navController.currentBackStackEntryAsState().value?.destination?.route == "order1" ||
                navController.currentBackStackEntryAsState().value?.destination?.route == "productdetails"
                || navController.currentBackStackEntryAsState().value?.destination?.route == "favourite1"
                || navController.currentBackStackEntryAsState().value?.destination?.route == "cart"
                || navController.currentBackStackEntryAsState().value?.destination?.route == "summary"
                || navController.currentBackStackEntryAsState().value?.destination?.route == "success") {

                Unit
            } else {
                BottomNavigationBar(navController = navController)
            }


        }) { padding ->


            NavigationController(navController = navController, padding = padding,istheme)


        }
    }


}



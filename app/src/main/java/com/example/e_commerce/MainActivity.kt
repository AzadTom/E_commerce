package com.example.e_commerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.e_commerce.Screen.*
import com.example.e_commerce.ui.theme.EcommerceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EcommerceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {

                    Navigation()
                }
            }
        }
    }




    @Composable
    fun NavigationController(navController: NavHostController) {

        NavHost(
            navController = navController,
            startDestination = E_commerce.Home.route
        ) {

            composable(route = E_commerce.Home.route) {
                Home()
            }

            composable(route = E_commerce.Order.route) {
                Order()
            }

            composable(route = E_commerce.Cart.route) {
                Cart()
            }

            composable(route = E_commerce.Account.route) {
                Account(){

                    navController.navigate("publish")

                }
            }

            composable(route = "publish") {
                PublishScreen()
            }



        }


    }

    @Composable
    fun BottomNavigationBar(navController: NavController) {

        val items = listOf(
            E_commerce.Home, E_commerce.Order, E_commerce.Cart, E_commerce.Account
        )

        BottomNavigation(
            backgroundColor = Color.White,
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


    @Composable
    fun Navigation() {

        val navController = rememberNavController()


        Scaffold(bottomBar = {

            if( navController.currentBackStackEntryAsState().value?.destination?.route != "publish" )
            {
                BottomNavigationBar(navController = navController)
            }
            else
            {
                Unit
            }



        }) { paddingvalue->


            NavigationController(navController = navController)




        }
    }
}



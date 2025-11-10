package com.example.projectprm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.projectprm.navigation.Screen
import com.example.projectprm.ui.screens.*
import com.example.projectprm.ui.theme.ProjectPrmTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectPrmTheme {
                BookStoreApp()
            }
        }
    }
}

@Composable
fun BookStoreApp() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        // Authentication
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigateUp()
                }
            )
        }
        
        // Main App
        composable(Screen.Home.route) {
            HomeScreen(
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onCartClick = {
                    navController.navigate(Screen.Cart.route)
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onOrdersClick = {
                    navController.navigate(Screen.Orders.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
        
        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: 0
            BookDetailScreen(
                bookId = bookId,
                onBackClick = {
                    navController.navigateUp()
                },
                onAddToCartClick = { quantity ->
                    // TODO: Implement add to cart logic
                    navController.navigate(Screen.Cart.route)
                }
            )
        }
        
        composable(Screen.Cart.route) {
            CartScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onCheckoutClick = {
                    navController.navigate(Screen.Checkout.route)
                }
            )
        }
        
        composable(Screen.Checkout.route) {
            CheckoutScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onOrderPlaced = { orderId ->
                    // Navigate to Orders screen after successful order
                    navController.navigate(Screen.Orders.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }
        
        composable(Screen.Orders.route) {
            OrdersScreen(
                onBackClick = {
                    navController.navigateUp()
                },
                onOrderClick = { orderId ->
                    navController.navigate(Screen.OrderDetail.createRoute(orderId))
                }
            )
        }
        
        composable(
            route = Screen.OrderDetail.route,
            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
            OrderDetailScreen(
                orderId = orderId,
                onBackClick = {
                    navController.navigateUp()
                },
                onReorderClick = {
                    // TODO: Implement reorder logic
                    navController.navigate(Screen.Cart.route)
                }
            )
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onEditProfileClick = {
                    // TODO: Implement edit profile
                },
                onMyOrdersClick = {
                    navController.navigate(Screen.Orders.route)
                },
                onSettingsClick = {
                    // TODO: Implement settings
                },
                onLogoutClick = {
                    // TODO: Implement logout logic
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Search.route) {
            // TODO: Implement SearchScreen
            HomeScreen(
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onCartClick = {
                    navController.navigate(Screen.Cart.route)
                },
                onSearchClick = { },
                onOrdersClick = {
                    navController.navigate(Screen.Orders.route)
                },
                onProfileClick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
    }
}
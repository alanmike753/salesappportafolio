package com.alandiaz.salesappportfolio.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.alandiaz.salesappportfolio.ui.screens.CartScreen
import com.alandiaz.salesappportfolio.ui.screens.LoginScreen
import com.alandiaz.salesappportfolio.ui.screens.OrderHistoryScreen
import com.alandiaz.salesappportfolio.ui.screens.ProductDetailScreen
import com.alandiaz.salesappportfolio.ui.screens.ProductListScreen
import com.alandiaz.salesappportfolio.ui.screens.ProfileScreen // <-- NUEVA IMPORTACIÓN
import com.alandiaz.salesappportfolio.ui.screens.RegisterScreen
import com.alandiaz.salesappportfolio.viewmodel.AuthViewModel

// Define las rutas como constantes
object AppDestinations {
    const val AUTH_GRAPH_ROUTE = "authGraph"
    const val LOGIN_ROUTE = "login"
    const val REGISTER_ROUTE = "register"

    const val MAIN_APP_GRAPH_ROUTE = "mainAppGraph"
    const val PRODUCT_LIST_ROUTE = "productList"
    const val PRODUCT_DETAIL_ROUTE = "productDetail"
    const val CART_ROUTE = "cart"
    const val ORDER_HISTORY_ROUTE = "orderHistory"
    const val PROFILE_ROUTE = "profile" // <-- NUEVA RUTA
    const val PRODUCT_ID_ARG = "productId"
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val startDestination = if (currentUser != null) {
        AppDestinations.MAIN_APP_GRAPH_ROUTE
    } else {
        AppDestinations.AUTH_GRAPH_ROUTE
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Grafo de Autenticación
        composable(route = AppDestinations.AUTH_GRAPH_ROUTE) {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(AppDestinations.MAIN_APP_GRAPH_ROUTE) {
                        popUpTo(AppDestinations.AUTH_GRAPH_ROUTE) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AppDestinations.REGISTER_ROUTE)
                }
            )
        }

        composable(route = AppDestinations.REGISTER_ROUTE) {
            RegisterScreen(
                navController = navController,
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(AppDestinations.MAIN_APP_GRAPH_ROUTE) {
                        popUpTo(AppDestinations.AUTH_GRAPH_ROUTE) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Grafo Principal de la Aplicación
        composable(route = AppDestinations.MAIN_APP_GRAPH_ROUTE) {
            ProductListScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(route = AppDestinations.CART_ROUTE) {
            CartScreen(navController = navController)
        }

        composable(route = AppDestinations.ORDER_HISTORY_ROUTE) {
            OrderHistoryScreen(navController = navController)
        }

        // AÑADIDO: Ruta para la pantalla del perfil de usuario
        composable(route = AppDestinations.PROFILE_ROUTE) {
            ProfileScreen(navController = navController, authViewModel = authViewModel)
        }

        composable(
            route = "${AppDestinations.PRODUCT_DETAIL_ROUTE}/{${AppDestinations.PRODUCT_ID_ARG}}",
            arguments = listOf(navArgument(AppDestinations.PRODUCT_ID_ARG) { type = NavType.IntType })
        ) {
            ProductDetailScreen(navController = navController)
        }
    }
}
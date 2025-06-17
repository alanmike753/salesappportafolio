package com.alandiaz.salesappportfolio.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.alandiaz.salesappportfolio.data.local.AppDatabase
import com.alandiaz.salesappportfolio.data.repository.CartRepository
import com.alandiaz.salesappportfolio.data.repository.OrderRepository
import com.alandiaz.salesappportfolio.data.repository.ProductRepository
import com.alandiaz.salesappportfolio.ui.components.ProductItem
import com.alandiaz.salesappportfolio.ui.navigation.AppDestinations
import com.alandiaz.salesappportfolio.ui.theme.SalesAppPortfolioTheme
import com.alandiaz.salesappportfolio.viewmodel.AuthViewModel
import com.alandiaz.salesappportfolio.viewmodel.CartViewModel
import com.alandiaz.salesappportfolio.viewmodel.CartViewModelFactory
import com.alandiaz.salesappportfolio.viewmodel.ProductListViewModel
import com.alandiaz.salesappportfolio.viewmodel.ProductListViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel = viewModel() // No necesitamos pasar este authViewModel si no lo usamos aquí
) {
    // --- Creación de dependencias (forma temporal hasta usar Hilt) ---
    val context = LocalContext.current.applicationContext
    val appDatabase = AppDatabase.getInstance(context)
    val productRepository = ProductRepository(appDatabase.productDao())
    val cartRepository = CartRepository(appDatabase.cartDao())
    val orderRepository = OrderRepository(appDatabase.orderDao(), appDatabase.cartDao())
    val productListViewModelFactory = ProductListViewModelFactory(productRepository)
    val cartViewModelFactory = CartViewModelFactory(cartRepository, orderRepository)
    // --- Fin de creación de dependencias ---

    val productListViewModel: ProductListViewModel = viewModel(factory = productListViewModelFactory)
    val cartViewModel: CartViewModel = viewModel(factory = cartViewModelFactory)

    val productsList by productListViewModel.products.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    SalesAppPortfolioTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = { Text("Catálogo de Productos") },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        // Botón para ir al carrito
                        IconButton(onClick = { navController.navigate(AppDestinations.CART_ROUTE) }) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito de Compras"
                            )
                        }
                        // Botón para ir al perfil de usuario
                        IconButton(onClick = { navController.navigate(AppDestinations.PROFILE_ROUTE) }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Perfil de Usuario"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                if (productsList.isEmpty()) {
                    Text(
                        text = "No hay productos disponibles.",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = productsList,
                            key = { product -> product.productId }
                        ) { product ->
                            ProductItem(
                                product = product,
                                onItemClick = { productId ->
                                    navController.navigate("${AppDestinations.PRODUCT_DETAIL_ROUTE}/$productId")
                                },
                                onAddToCartClick = { productId ->
                                    cartViewModel.addProductToCart(productId)
                                    scope.launch {
                                        snackbarHostState.showSnackbar("¡Producto añadido al carrito!")
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
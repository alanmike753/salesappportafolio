package com.alandiaz.salesappportfolio.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image // <-- NUEVA IMPORTACIÓN
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale // <-- NUEVA IMPORTACIÓN
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource // <-- NUEVA IMPORTACIÓN
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.alandiaz.salesappportfolio.data.local.AppDatabase
import com.alandiaz.salesappportfolio.data.local.CartItemWithProduct
import com.alandiaz.salesappportfolio.data.repository.CartRepository
import com.alandiaz.salesappportfolio.data.repository.OrderRepository
import com.alandiaz.salesappportfolio.ui.theme.SalesAppPortfolioTheme
import com.alandiaz.salesappportfolio.viewmodel.AuthViewModel
import com.alandiaz.salesappportfolio.viewmodel.CartViewModel
import com.alandiaz.salesappportfolio.viewmodel.CartViewModelFactory
import com.alandiaz.salesappportfolio.viewmodel.CheckoutState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    // --- Creación de dependencias (forma temporal hasta usar Hilt) ---
    val context = LocalContext.current.applicationContext
    val appDatabase = AppDatabase.getInstance(context)
    val cartRepository = CartRepository(appDatabase.cartDao())
    val orderRepository = OrderRepository(appDatabase.orderDao(), appDatabase.cartDao())
    val factory = CartViewModelFactory(cartRepository, orderRepository)
    // --- Fin de creación de dependencias ---

    val cartViewModel: CartViewModel = viewModel(factory = factory)
    val cartUiState by cartViewModel.cartUiState.collectAsState()
    val checkoutState by cartViewModel.checkoutState.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    // Efecto para manejar los resultados del proceso de checkout
    LaunchedEffect(key1 = checkoutState) {
        when (val state = checkoutState) {
            is CheckoutState.Success -> {
                Toast.makeText(context, "¡Pedido realizado con éxito!", Toast.LENGTH_LONG).show()
                cartViewModel.resetCheckoutState()
                navController.popBackStack()
            }
            is CheckoutState.Error -> {
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                cartViewModel.resetCheckoutState()
            }
            else -> Unit
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        },
        bottomBar = {
            if (cartUiState.cartItems.isNotEmpty()) {
                BottomAppBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total:", style = MaterialTheme.typography.bodyMedium)
                            Text(
                                "$${String.format("%.2f", cartUiState.totalPrice)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Button(
                            onClick = { cartViewModel.placeOrder(currentUser) },
                            enabled = checkoutState !is CheckoutState.Loading
                        ) {
                            if (checkoutState is CheckoutState.Loading) {
                                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                            } else {
                                Text("Finalizar Compra")
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        if (cartUiState.cartItems.isEmpty() && checkoutState !is CheckoutState.Success) {
            Box(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding).fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(items = cartUiState.cartItems, key = { it.cartItemId }) { item ->
                    CartItemRow(
                        item = item,
                        onIncreaseQuantity = { cartViewModel.addProductToCart(item.productId) },
                        onDecreaseQuantity = { cartViewModel.updateItemQuantity(item.productId, item.quantity - 1) },
                        onRemoveItem = { cartViewModel.removeProductFromCart(item.productId) }
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItemWithProduct,
    onIncreaseQuantity: () -> Unit,
    onDecreaseQuantity: () -> Unit,
    onRemoveItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // CAMBIO: Reemplazado Box con Image para mostrar la imagen local
            Image(
                painter = painterResource(id = item.imageResId),
                contentDescription = "Imagen de ${item.name}",
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(
                    "$${String.format("%.2f", item.price)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecreaseQuantity, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Remove, "Quitar uno")
                }
                Text("${item.quantity}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 8.dp))
                IconButton(onClick = onIncreaseQuantity, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Add, "Añadir uno")
                }
                IconButton(onClick = onRemoveItem, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Delete, "Eliminar del carrito", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CartScreenPreview_Empty() {
    SalesAppPortfolioTheme {
        CartScreen(navController = NavHostController(LocalContext.current))
    }
}


package com.alandiaz.salesappportfolio.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image // <-- NUEVA IMPORTACIÓN
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale // <-- NUEVA IMPORTACIÓN
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource // <-- NUEVA IMPORTACIÓN
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.alandiaz.salesappportfolio.data.local.AppDatabase
import com.alandiaz.salesappportfolio.data.repository.CartRepository
import com.alandiaz.salesappportfolio.data.repository.ProductRepository
import com.alandiaz.salesappportfolio.ui.theme.SalesAppPortfolioTheme
import com.alandiaz.salesappportfolio.utils.findActivity
import com.alandiaz.salesappportfolio.viewmodel.ProductDetailViewModel
import com.alandiaz.salesappportfolio.viewmodel.ProductDetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // --- Creación de dependencias (forma temporal hasta usar Hilt) ---
    val context = LocalContext.current
    val owner = context.findActivity() as? androidx.savedstate.SavedStateRegistryOwner
        ?: throw IllegalStateException("No SavedStateRegistryOwner found from the context")

    val appDatabase = AppDatabase.getInstance(context.applicationContext)
    val productRepository = ProductRepository(appDatabase.productDao())
    val cartRepository = CartRepository(appDatabase.cartDao())
    val factory = ProductDetailViewModelFactory(owner, productRepository, cartRepository)
    // --- Fin de creación de dependencias ---

    val viewModel: ProductDetailViewModel = viewModel(factory = factory)
    val product by viewModel.product.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "Detalle del Producto") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        bottomBar = {
            product?.let {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Button(
                        onClick = {
                            viewModel.addToCart()
                            // Muestra un mensaje de confirmación
                            Toast.makeText(context, "${it.name} añadido al carrito", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.AddShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Añadir al Carrito")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Para que la descripción sea scrollable
        ) {
            if (product == null) {
                // Muestra un indicador de carga mientras se obtiene el producto
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // SECCIÓN DE IMAGEN ACTUALIZADA
                Image(
                    painter = painterResource(id = product!!.imageResId),
                    contentDescription = "Imagen de ${product!!.name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop // Escala la imagen para que cubra el área
                )

                // Detalles del producto (se mantienen igual)
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = product!!.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$${String.format("%.2f", product!!.price)}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product!!.description,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
package com.alandiaz.salesappportfolio.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember // <-- IMPORTACIÓN AÑADIDA
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.alandiaz.salesappportfolio.data.local.AppDatabase
import com.alandiaz.salesappportfolio.data.local.OrderEntity
import com.alandiaz.salesappportfolio.data.repository.OrderRepository
import com.alandiaz.salesappportfolio.viewmodel.OrderHistoryViewModel
import com.alandiaz.salesappportfolio.viewmodel.OrderHistoryViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // --- Creación de dependencias (forma temporal hasta usar Hilt) ---
    val context = LocalContext.current.applicationContext
    val appDatabase = AppDatabase.getInstance(context)
    val orderRepository = OrderRepository(appDatabase.orderDao(), appDatabase.cartDao())
    val factory = OrderHistoryViewModelFactory(orderRepository)
    // --- Fin de creación de dependencias ---

    val orderHistoryViewModel: OrderHistoryViewModel = viewModel(factory = factory)
    val orderHistory by orderHistoryViewModel.orderHistory.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Mi Historial de Pedidos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (orderHistory.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("No has realizado ningún pedido todavía.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = orderHistory, key = { it.orderId }) { order ->
                    OrderHistoryItem(order = order)
                }
            }
        }
    }
}

@Composable
fun OrderHistoryItem(
    order: OrderEntity,
    modifier: Modifier = Modifier
) {
    // Formateador de fecha para mostrarla de manera legible
    val dateFormatter = remember {
        SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Pedido #${order.orderId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dateFormatter.format(order.orderDate),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "Total: $${String.format("%.2f", order.totalAmount)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
            // En el futuro, se podría hacer clic aquí para ver los detalles completos del pedido
        }
    }
}
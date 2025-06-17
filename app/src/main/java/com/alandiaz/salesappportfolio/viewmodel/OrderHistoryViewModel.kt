package com.alandiaz.salesappportfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alandiaz.salesappportfolio.data.local.OrderEntity
import com.alandiaz.salesappportfolio.data.repository.OrderRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrderHistoryViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _orderHistory = MutableStateFlow<List<OrderEntity>>(emptyList())
    val orderHistory: StateFlow<List<OrderEntity>> = _orderHistory.asStateFlow()

    init {
        // Observa al usuario actual y carga su historial de pedidos.
        // Se usa collectLatest para que si el usuario cambia (ej. cierra sesión y otro inicia),
        // la coroutine anterior se cancele y se inicie una nueva para el nuevo usuario.
        viewModelScope.launch {
            FirebaseAuth.getInstance().currentUser?.uid?.let { userId ->
                orderRepository.getOrderHistory(userId).collect { orders ->
                    _orderHistory.value = orders
                }
            }
        }
    }

    /**
     * Una función que podría ser llamada explícitamente para refrescar el historial,
     * aunque el Flow debería hacerlo automáticamente.
     */
    fun loadOrderHistory() {
        viewModelScope.launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                orderRepository.getOrderHistory(userId).collect { orders ->
                    _orderHistory.value = orders
                }
            } else {
                _orderHistory.value = emptyList() // Limpia el historial si no hay usuario
            }
        }
    }
}
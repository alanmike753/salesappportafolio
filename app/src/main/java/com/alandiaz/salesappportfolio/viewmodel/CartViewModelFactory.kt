package com.alandiaz.salesappportfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alandiaz.salesappportfolio.data.repository.CartRepository
import com.alandiaz.salesappportfolio.data.repository.OrderRepository

/**
 * Fábrica actualizada para crear instancias de CartViewModel.
 * Ahora provee tanto CartRepository como OrderRepository.
 */
class CartViewModelFactory(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository // <-- Dependencia del OrderRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CartViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Pasamos ambas dependencias al crear el CartViewModel
            return CartViewModel(cartRepository, orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
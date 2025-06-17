package com.alandiaz.salesappportfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alandiaz.salesappportfolio.data.repository.OrderRepository

/**
 * Fábrica para crear instancias de OrderHistoryViewModel, ya que requiere OrderRepository
 * en su constructor.
 */
class OrderHistoryViewModelFactory(
    private val orderRepository: OrderRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return OrderHistoryViewModel(orderRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}

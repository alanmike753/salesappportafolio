package com.alandiaz.salesappportfolio.viewmodel // Asegúrate que el paquete sea el correcto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alandiaz.salesappportfolio.data.repository.ProductRepository

class ProductListViewModelFactory(
    private val productRepository: ProductRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductListViewModel(productRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
package com.alandiaz.salesappportfolio.viewmodel

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.alandiaz.salesappportfolio.data.repository.CartRepository
import com.alandiaz.salesappportfolio.data.repository.ProductRepository

/**
 * Fábrica para crear instancias de ProductDetailViewModel.
 * Utiliza AbstractSavedStateViewModelFactory para poder inyectar el SavedStateHandle
 * junto con otras dependencias (los repositorios).
 */
class ProductDetailViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle // El SavedStateHandle es provisto por la fábrica
    ): T {
        if (modelClass.isAssignableFrom(ProductDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Creamos el ViewModel pasándole las dependencias y el handle
            return ProductDetailViewModel(productRepository, cartRepository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
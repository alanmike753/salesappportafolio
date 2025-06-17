package com.alandiaz.salesappportfolio.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alandiaz.salesappportfolio.data.local.ProductEntity
import com.alandiaz.salesappportfolio.data.repository.CartRepository
import com.alandiaz.salesappportfolio.data.repository.ProductRepository
import com.alandiaz.salesappportfolio.ui.navigation.AppDestinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle // Se usa para acceder a los argumentos de navegación
) : ViewModel() {

    private val _product = MutableStateFlow<ProductEntity?>(null)
    val product: StateFlow<ProductEntity?> = _product.asStateFlow()

    init {
        // Obtiene el productId del SavedStateHandle, que es proveído por Navigation Compose.
        val productId: Int? = savedStateHandle[AppDestinations.PRODUCT_ID_ARG]
        if (productId != null) {
            loadProductDetails(productId)
        }
    }

    private fun loadProductDetails(productId: Int) {
        viewModelScope.launch {
            productRepository.getProductById(productId).collectLatest { productEntity ->
                _product.value = productEntity
            }
        }
    }

    /**
     * Añade el producto actual al carrito de compras.
     */
    fun addToCart() {
        viewModelScope.launch {
            _product.value?.let { currentProduct ->
                cartRepository.addProductToCart(currentProduct.productId)
            }
        }
    }
}
package com.alandiaz.salesappportfolio.viewmodel // Asegúrate que el paquete sea el correcto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alandiaz.salesappportfolio.data.local.ProductEntity
import com.alandiaz.salesappportfolio.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductListViewModel(private val productRepository: ProductRepository) : ViewModel() {

    // _products es un MutableStateFlow privado que contiene el estado actual de la lista de productos.
    // Lo inicializamos con una lista vacía.
    private val _products = MutableStateFlow<List<ProductEntity>>(emptyList())

    // products es la versión pública e inmutable de _products, expuesta como StateFlow.
    // La UI observará este StateFlow para las actualizaciones.
    val products: StateFlow<List<ProductEntity>> = _products.asStateFlow()

    // Podríamos añadir otro StateFlow para el estado de carga o errores, por ejemplo:
    // private val _isLoading = MutableStateFlow<Boolean>(false)
    // val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    //
    // private val _error = MutableStateFlow<String?>(null)
    // val error: StateFlow<String?> = _error.asStateFlow()

    init {
        // Cuando el ViewModel es creado, lanzamos una coroutine para cargar los productos.
        loadProducts()
    }

    private fun loadProducts() {
        // Usamos viewModelScope, que es un CoroutineScope atado al ciclo de vida del ViewModel.
        // Se cancelará automáticamente cuando el ViewModel sea destruido.
        viewModelScope.launch {
            // _isLoading.value = true // Si tuviéramos estado de carga
            // _error.value = null
            productRepository.getAllProducts()
                .catch { exception ->
                    // Manejar el error, por ejemplo, actualizando un StateFlow de error
                    // _error.value = "Error al cargar productos: ${exception.message}"
                    // _isLoading.value = false
                    // Por ahora, solo imprimimos en logcat para simplificar
                    println("Error al cargar productos: ${exception.message}")
                    _products.value = emptyList() // Opcional: mostrar lista vacía en error
                }
                .collect { productList ->
                    // Cuando el Flow emite una nueva lista de productos, actualizamos nuestro StateFlow.
                    _products.value = productList
                    // _isLoading.value = false // Si tuviéramos estado de carga
                }
        }
    }

    // Aquí podrías añadir otras funciones para manejar interacciones del usuario,
    // como añadir un producto al carrito, refrescar la lista, etc.
    // Por ejemplo:
    // fun refreshProducts() {
    //     loadProducts()
    // }
}
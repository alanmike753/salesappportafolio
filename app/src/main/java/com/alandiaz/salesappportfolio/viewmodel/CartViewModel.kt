package com.alandiaz.salesappportfolio.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alandiaz.salesappportfolio.data.local.CartItemWithProduct
import com.alandiaz.salesappportfolio.data.local.UserEntity
import com.alandiaz.salesappportfolio.data.repository.CartRepository
import com.alandiaz.salesappportfolio.data.repository.OrderRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Estado de la UI para el contenido del carrito
data class CartUiState(
    val cartItems: List<CartItemWithProduct> = emptyList(),
    val totalPrice: Double = 0.0
)

// Nuevo estado para el proceso de finalizar compra (checkout)
sealed interface CheckoutState {
    data object Idle : CheckoutState
    data object Loading : CheckoutState
    data object Success : CheckoutState
    data class Error(val message: String) : CheckoutState
}

class CartViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    // StateFlow para el estado del carrito
    val cartUiState: StateFlow<CartUiState> =
        cartRepository.getCartContents()
            .map { items ->
                val total = items.sumOf { it.price * it.quantity }
                CartUiState(cartItems = items, totalPrice = total)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = CartUiState()
            )

    // StateFlow para el estado del proceso de checkout
    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState.asStateFlow()

    fun placeOrder(firebaseUser: FirebaseUser?) {
        viewModelScope.launch {
            if (firebaseUser == null) {
                _checkoutState.value = CheckoutState.Error("Debes iniciar sesión para realizar un pedido.")
                return@launch
            }
            val currentCartItems = cartUiState.value.cartItems
            if (currentCartItems.isEmpty()) {
                _checkoutState.value = CheckoutState.Error("El carrito está vacío.")
                return@launch
            }
            _checkoutState.value = CheckoutState.Loading
            try {
                val userEntity = UserEntity(
                    userId = firebaseUser.uid,
                    email = firebaseUser.email ?: "No email",
                    displayName = firebaseUser.displayName
                )
                orderRepository.placeOrder(userEntity, currentCartItems)
                _checkoutState.value = CheckoutState.Success
            } catch (e: Exception) {
                _checkoutState.value = CheckoutState.Error(e.message ?: "Ocurrió un error inesperado.")
            }
        }
    }

    fun resetCheckoutState() {
        _checkoutState.value = CheckoutState.Idle
    }

    fun addProductToCart(productId: Int) {
        viewModelScope.launch { cartRepository.addProductToCart(productId) }
    }

    fun removeProductFromCart(productId: Int) {
        viewModelScope.launch { cartRepository.removeProductFromCart(productId) }
    }

    fun updateItemQuantity(productId: Int, newQuantity: Int) {
        // La línea de abajo es la que ha sido corregida
        viewModelScope.launch { cartRepository.updateCartItemQuantity(productId, newQuantity) }
    }

    fun clearCart() {
        viewModelScope.launch { cartRepository.clearCart() }
    }
}
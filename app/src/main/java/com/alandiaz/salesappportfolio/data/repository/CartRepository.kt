package com.alandiaz.salesappportfolio.data.repository

import com.alandiaz.salesappportfolio.data.local.CartDao
import com.alandiaz.salesappportfolio.data.local.CartItemEntity
import com.alandiaz.salesappportfolio.data.local.CartItemWithProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repositorio para gestionar las operaciones del carrito de compras.
 * Abstrae el origen de los datos del carrito (en este caso, el CartDao local).
 */
class CartRepository(private val cartDao: CartDao) {

    /**
     * Obtiene todos los artículos del carrito con los detalles del producto como un Flow.
     * La UI observará este Flow para recibir actualizaciones en tiempo real.
     */
    fun getCartContents(): Flow<List<CartItemWithProduct>> {
        return cartDao.getCartItemsWithProducts()
    }

    /**
     * Añade un producto al carrito.
     * Si el producto ya está en el carrito, incrementa su cantidad.
     * Si no, inserta un nuevo artículo en el carrito con cantidad 1.
     * Esta operación se ejecuta en el hilo de IO.
     */
    suspend fun addProductToCart(productId: Int) {
        withContext(Dispatchers.IO) {
            val existingCartItem = cartDao.getCartItemByProductId(productId)

            if (existingCartItem != null) {
                // El producto ya está en el carrito, actualiza la cantidad
                val updatedItem = existingCartItem.copy(quantity = existingCartItem.quantity + 1)
                cartDao.update(updatedItem)
            } else {
                // El producto no está en el carrito, inserta uno nuevo
                val newCartItem = CartItemEntity(productId = productId, quantity = 1)
                cartDao.insert(newCartItem)
            }
        }
    }

    /**
     * Actualiza la cantidad de un artículo en el carrito.
     * Si la cantidad es 0 o menos, elimina el artículo.
     */
    suspend fun updateCartItemQuantity(productId: Int, newQuantity: Int) {
        withContext(Dispatchers.IO) {
            val existingCartItem = cartDao.getCartItemByProductId(productId)
            if (existingCartItem != null) {
                if (newQuantity > 0) {
                    val updatedItem = existingCartItem.copy(quantity = newQuantity)
                    cartDao.update(updatedItem)
                } else {
                    // Si la nueva cantidad es 0 o menos, elimina el artículo
                    cartDao.delete(existingCartItem)
                }
            }
        }
    }

    /**
     * Elimina un producto del carrito, sin importar su cantidad.
     */
    suspend fun removeProductFromCart(productId: Int) {
        withContext(Dispatchers.IO) {
            val existingCartItem = cartDao.getCartItemByProductId(productId)
            if (existingCartItem != null) {
                cartDao.delete(existingCartItem)
            }
        }
    }

    /**
     * Vacía completamente el carrito de compras.
     */
    suspend fun clearCart() {
        withContext(Dispatchers.IO) {
            cartDao.clearCart()
        }
    }
}
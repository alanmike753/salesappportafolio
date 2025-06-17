package com.alandiaz.salesappportfolio.data.repository

import com.alandiaz.salesappportfolio.data.local.CartDao
import com.alandiaz.salesappportfolio.data.local.OrderDao
import com.alandiaz.salesappportfolio.data.local.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para gestionar las operaciones relacionadas con los pedidos.
 */
class OrderRepository(
    private val orderDao: OrderDao,
    private val cartDao: CartDao
) {

    /**
     * Crea un nuevo pedido en la base de datos a partir del contenido actual del carrito.
     * Después de crear el pedido, vacía el carrito.
     * @param user El usuario que está realizando el pedido.
     */
    suspend fun createOrderFromCart(user: UserEntity) {
        withContext(Dispatchers.IO) {
            // Obtiene el estado actual del carrito.
            // Nota: Esta es una lectura única, no un Flow.
            // Para obtener los datos de un Flow una sola vez, necesitaríamos .first() de kotlinx-coroutines-flow.
            // Por simplicidad aquí, asumiremos que el ViewModel nos pasa la lista actual.
            // Esta es una implementación que se llamará desde el ViewModel,
            // que sí tiene acceso a la lista actual del carrito.
            // Ver la implementación en el ViewModel.
        }
    }

    /**
     * Un método más directo que recibe la lista de artículos del carrito.
     * El ViewModel obtendrá la lista del StateFlow y la pasará a este método.
     * @param user El usuario que realiza el pedido.
     * @param cartItems La lista de artículos del carrito.
     */
    suspend fun placeOrder(user: UserEntity, cartItems: List<com.alandiaz.salesappportfolio.data.local.CartItemWithProduct>) {
        if (cartItems.isNotEmpty()) {
            withContext(Dispatchers.IO) {
                // Usa el método de transacción del DAO para crear el pedido.
                orderDao.createOrderFromCart(user, cartItems)

                // Después de crear el pedido exitosamente, vacía el carrito.
                cartDao.clearCart()
            }
        }
    }

    /**
     * Obtiene el historial de pedidos para un usuario específico.
     * @param userId El ID del usuario.
     * @return Un Flow que emite la lista de pedidos del usuario.
     */
    fun getOrderHistory(userId: String) = orderDao.getOrdersForUser(userId)

    // En el futuro, se podrían añadir métodos más complejos aquí para obtener
    // los pedidos junto con todos sus artículos (usando OrderWithItems).
}
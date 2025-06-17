package com.alandiaz.salesappportfolio.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import java.util.Date

/**
 * Clase de datos para representar un artículo de pedido junto con los detalles del producto.
 */
data class OrderItemWithProductDetails(
    val productId: Int?,
    val quantity: Int,
    val priceAtPurchase: Double,
    val productName: String?, // Nombre del producto en el momento de la compra
    val imageUrl: String?     // URL de la imagen del producto
)

/**
 * Clase de datos para representar un pedido completo con su lista de artículos.
 */
data class OrderWithItems(
    val orderId: Int,
    val userId: String,
    val orderDate: Date,
    val totalAmount: Double,
    val items: List<OrderItemWithProductDetails>
)

@Dao
interface OrderDao {

    /**
     * Inserta un pedido principal (OrderEntity) y devuelve su ID autogenerado.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity): Long // Devuelve el ID de la fila insertada

    /**
     * Inserta una lista de artículos de pedido (OrderItemEntity).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    /**
     * Inserta un usuario. Se usa para asegurar que el usuario existe antes de crear un pedido.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignora si el usuario ya existe
    suspend fun insertUser(user: UserEntity)

    /**
     * Método de conveniencia que envuelve la creación de un pedido completo en una transacción.
     * Esto asegura que o todo se guarda correctamente, o no se guarda nada.
     */
    @Transaction
    suspend fun createOrderFromCart(user: UserEntity, cartItems: List<CartItemWithProduct>) {
        // 1. Asegurarse de que el usuario esté en la base de datos
        insertUser(user)

        // 2. Crear y guardar el pedido principal (OrderEntity)
        val total = cartItems.sumOf { it.price * it.quantity }
        val order = OrderEntity(
            userId = user.userId,
            orderDate = Date(), // Fecha actual
            totalAmount = total
        )
        val orderId = insertOrder(order).toInt()

        // 3. Convertir los artículos del carrito en artículos de pedido y guardarlos
        val orderItems = cartItems.map { cartItem ->
            OrderItemEntity(
                orderId = orderId,
                productId = cartItem.productId,
                quantity = cartItem.quantity,
                priceAtPurchase = cartItem.price
            )
        }
        insertOrderItems(orderItems)
    }

    /**
     * Obtiene todos los pedidos de un usuario específico.
     * Esta es una consulta compleja que requerirá un método adicional para ensamblar los datos.
     * Por ahora, definimos la consulta para obtener los pedidos principales.
     */
    @Query("SELECT * FROM orders WHERE user_id = :userId ORDER BY order_date DESC")
    fun getOrdersForUser(userId: String): Flow<List<OrderEntity>>

    // En una implementación más avanzada, se podría crear una consulta
    // que devuelva directamente una lista de 'OrderWithItems' usando la anotación @Transaction
    // y una consulta con JOINs, pero para empezar, obtener los pedidos principales es suficiente.
}
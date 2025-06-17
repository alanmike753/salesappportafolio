package com.alandiaz.salesappportfolio.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Representa un artículo específico dentro de un pedido.
 * Por ejemplo, si un pedido contiene 3 laptops y 2 teclados, habrá dos filas
 * en esta tabla para ese pedido.
 *
 * @param orderItemId El ID único autogenerado para este artículo del pedido.
 * @param orderId El ID del pedido al que pertenece este artículo.
 * @param productId El ID del producto que fue comprado.
 * @param quantity La cantidad de este producto que se compró.
 * @param priceAtPurchase El precio del producto en el momento de la compra.
 */
@Entity(
    tableName = "order_items",
    // Define dos claves foráneas: una hacia la tabla 'orders' y otra hacia 'products'.
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["order_id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE // Si un pedido es eliminado, sus artículos también lo son.
        ),
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["product_id"],
            childColumns = ["product_id"],
            // Usamos SET_NULL aquí para que si un producto se elimina del catálogo,
            // el historial de pedidos no se borre, solo se desvincule el producto.
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_item_id")
    val orderItemId: Int = 0,

    @ColumnInfo(name = "order_id", index = true)
    val orderId: Int,

    // Hacemos el productId nulable para que coincida con onDelete = SET_NULL.
    @ColumnInfo(name = "product_id", index = true)
    val productId: Int?,

    @ColumnInfo(name = "quantity")
    val quantity: Int,

    @ColumnInfo(name = "price_at_purchase")
    val priceAtPurchase: Double
)
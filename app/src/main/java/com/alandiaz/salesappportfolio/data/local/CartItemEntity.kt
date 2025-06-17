package com.alandiaz.salesappportfolio.data.local // Asegúrate que el paquete sea el correcto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Representa un artículo único en el carrito de compras.
 *
 * @param cartItemId El ID único autogenerado para este artículo del carrito.
 * @param productId El ID del producto que se añadió al carrito. Es una clave foránea a la tabla 'products'.
 * @param quantity La cantidad de este producto en el carrito.
 */
@Entity(
    tableName = "cart_items",
    // Define una clave foránea para vincular esta tabla con la tabla 'products'.
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,      // La tabla padre.
            parentColumns = ["product_id"],     // La columna de la clave primaria en la tabla padre.
            childColumns = ["product_id"],      // La columna en esta tabla que es la clave foránea.
            onDelete = ForeignKey.CASCADE       // Qué hacer si el producto padre se elimina: elimina también este artículo del carrito.
        )
    ],
    // Crea un índice en la columna 'product_id' para hacer las búsquedas más rápidas.
    // 'unique = true' asegura que solo puede haber una fila por cada productId.
    // Esto significa que en lugar de tener dos filas para "Laptop (cantidad 1)",
    // tendremos una sola fila "Laptop (cantidad 2)".
    indices = [Index(value = ["product_id"], unique = true)]
)
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cart_item_id")
    val cartItemId: Int = 0,

    @ColumnInfo(name = "product_id")
    val productId: Int,

    @ColumnInfo(name = "quantity")
    val quantity: Int
)

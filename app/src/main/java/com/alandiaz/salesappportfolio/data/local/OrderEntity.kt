package com.alandiaz.salesappportfolio.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Representa la información general de un pedido realizado por un usuario.
 *
 * @param orderId El ID único autogenerado para este pedido.
 * @param userId El ID del usuario que realizó el pedido. Es una clave foránea a la tabla 'users'.
 * @param orderDate La fecha en que se realizó el pedido.
 * @param totalAmount El monto total del pedido.
 */
@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE // Si un usuario es eliminado, sus pedidos también lo son.
        )
    ]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    val orderId: Int = 0,

    @ColumnInfo(name = "user_id", index = true) // 'index = true' para búsquedas más rápidas por usuario.
    val userId: String,

    @ColumnInfo(name = "order_date")
    val orderDate: Date,

    @ColumnInfo(name = "total_amount")
    val totalAmount: Double
)
package com.alandiaz.salesappportfolio.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// CAMBIO: ahora contiene imageResId (Int)
data class CartItemWithProduct(
    val cartItemId: Int,
    val productId: Int,
    val quantity: Int,
    val name: String,
    val price: Double,
    val imageResId: Int
)

@Dao
interface CartDao {

    // CAMBIO: La consulta ahora selecciona 'image_res_id'
    @Query("""
        SELECT 
            c.cart_item_id as cartItemId, 
            c.product_id as productId, 
            c.quantity, 
            p.name, 
            p.price, 
            p.image_res_id as imageResId
        FROM cart_items c
        INNER JOIN products p ON c.product_id = p.product_id
    """)
    fun getCartItemsWithProducts(): Flow<List<CartItemWithProduct>>

    // ... (el resto de los métodos se mantienen igual)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cartItem: CartItemEntity)

    @Update
    suspend fun update(cartItem: CartItemEntity)

    @Delete
    suspend fun delete(cartItem: CartItemEntity)

    @Query("SELECT * FROM cart_items WHERE product_id = :productId")
    suspend fun getCartItemByProductId(productId: Int): CartItemEntity?

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
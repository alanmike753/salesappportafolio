package com.alandiaz.salesappportfolio.data.local // Asegúrate que el paquete sea el correcto

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow // Importa Flow para consultas observables

@Dao // Anotación que identifica esta interfaz como un DAO para Room
interface ProductDao {

    // Inserta un producto. Si ya existe un producto con la misma clave primaria, lo reemplaza.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    // Inserta una lista de productos.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllProducts(products: List<ProductEntity>)

    // Actualiza un producto existente.
    @Update
    suspend fun updateProduct(product: ProductEntity)

    // Elimina un producto.
    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    // Obtiene todos los productos de la tabla, ordenados por nombre.
    // Devuelve un Flow para que la UI pueda observar cambios en los datos automáticamente.
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    // Obtiene un producto específico por su ID.
    // Devuelve un Flow para observar cambios en este producto específico.
    @Query("SELECT * FROM products WHERE product_id = :productId")
    fun getProductById(productId: Int): Flow<ProductEntity?> // Puede ser null si no se encuentra

    // (Opcional) Un método para eliminar todos los productos, útil para pruebas o reseteo.
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()
}
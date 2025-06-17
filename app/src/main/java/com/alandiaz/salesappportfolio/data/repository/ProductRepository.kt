package com.alandiaz.salesappportfolio.data.repository // Asegúrate que el paquete sea el correcto

import com.alandiaz.salesappportfolio.data.local.ProductDao
import com.alandiaz.salesappportfolio.data.local.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {

    // Obtiene todos los productos como un Flow. El ViewModel observará este Flow.
    fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    // Obtiene un producto específico por su ID como un Flow.
    fun getProductById(productId: Int): Flow<ProductEntity?> {
        return productDao.getProductById(productId)
    }

    // Inserta un nuevo producto.
    // Esta es una función suspendida, ya que realiza una operación de base de datos.
    suspend fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    // Actualiza un producto existente.
    suspend fun updateProduct(product: ProductEntity) {
        productDao.updateProduct(product)
    }

    // Elimina un producto.
    suspend fun deleteProduct(product: ProductEntity) {
        productDao.deleteProduct(product)
    }

    // (Opcional) Si necesitas una forma de insertar una lista de productos desde el repositorio
    suspend fun insertAllProducts(products: List<ProductEntity>) {
        productDao.insertAllProducts(products)
    }

    // (Opcional) Si necesitas una forma de eliminar todos los productos desde el repositorio
    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }
}
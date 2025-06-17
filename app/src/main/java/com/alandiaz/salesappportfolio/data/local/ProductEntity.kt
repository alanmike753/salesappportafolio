package com.alandiaz.salesappportfolio.data.local

import androidx.annotation.DrawableRes // <-- IMPORTACIÓN NECESARIA
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    val productId: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "price")
    val price: Double,

    // CAMBIO: Ahora es un Int que almacena el ID del recurso drawable (ej. R.drawable.plusnpk)
    @DrawableRes
    @ColumnInfo(name = "image_res_id")
    val imageResId: Int,

    @ColumnInfo(name = "category")
    val category: String?,

    @ColumnInfo(name = "stock")
    val stock: Int?
)
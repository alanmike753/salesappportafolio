package com.alandiaz.salesappportfolio.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alandiaz.salesappportfolio.R // <-- IMPORTACIÓN NECESARIA PARA R.drawable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        UserEntity::class,
        OrderEntity::class,
        OrderItemEntity::class
    ],
    version = 3, // La versión se mantiene en 3, ya que no hemos cambiado la estructura de las tablas.
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "sales_app_database"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database.productDao())
                    }
                }
            }
        }

        // Función actualizada para poblar la base de datos con tus productos
        suspend fun populateDatabase(productDao: ProductDao) {
            val product1 = ProductEntity(
                name = "HUMIPLUS",
                description = "Mejorador de suelo.",
                price = 750.00,
                imageResId = R.drawable.humiplus, // <-- CORREGIDO a imageResId
                category = "Mejorador de suelos",
                stock = 50
            )
            val product2 = ProductEntity(
                name = "PLUS NPK",
                description = "Fertilizante líquido.",
                price = 1000.00,
                imageResId = R.drawable.plusnpk, // <-- CORREGIDO
                category = "Fertilizante",
                stock = 120
            )
            val product3 = ProductEntity(
                name = "INDAZ-PLUS",
                description = "Biofertilizante foliar.",
                price = 1000.00,
                imageResId = R.drawable.indazplus, // <-- CORREGIDO
                category = "Foliar",
                stock = 75
            )
            val product4 = ProductEntity(
                name = "FULVIC Ca",
                description = "Biofertilizante foliar.",
                price = 900.00,
                imageResId = R.drawable.fulvicca, // <-- CORREGIDO (asegúrate que el archivo se llame fulvic_ca.png)
                category = "Foliar",
                stock = 75
            )
            val product5 = ProductEntity(
                name = "ULVA",
                description = "Bioestimulante.",
                price = 200.00,
                imageResId = R.drawable.ulva, // <-- CORREGIDO
                category = "Algas marinas",
                stock = 75
            )
            val product6 = ProductEntity(
                name = "RAIZ SUPREMA",
                description = "Regulador de crecimiento.",
                price = 300.00,
                imageResId = R.drawable.raizsuprema, // <-- CORREGIDO
                category = "Hormonal",
                stock = 75
            )
            val product7 = ProductEntity(
                name = "BIOGUANO",
                description = "Fertilizante orgánico.",
                price = 800.00,
                imageResId = R.drawable.bioguano, // <-- CORREGIDO
                category = "Orgánico",
                stock = 75
            )
            // No tengo una imagen para "MASS TALLOS" en tu captura, usaré un placeholder
            val product8 = ProductEntity(
                name = "MASS TALLOS",
                description = "Regulador de crecimiento.",
                price = 300.00,
                imageResId = R.drawable.plusnpk, // <-- Usando un placeholder, cámbialo si tienes la imagen
                category = "Hormonal",
                stock = 75
            )
            val product9 = ProductEntity(
                name = "AMINO PLUS",
                description = "Aminoácido.",
                price = 350.00,
                imageResId = R.drawable.aminoplus, // <-- CORREGIDO
                category = "Aminoácidos",
                stock = 75
            )

            productDao.insertAllProducts(listOf(product1, product2, product3, product4, product5, product6, product7, product8, product9))
        }
    }
}
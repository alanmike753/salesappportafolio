package com.alandiaz.salesappportfolio // Asegúrate que el paquete sea el correcto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController // <-- IMPORTACIÓN NECESARIA
import com.alandiaz.salesappportfolio.ui.navigation.AppNavHost // <-- IMPORTA TU AppNavHost
import com.alandiaz.salesappportfolio.ui.theme.SalesAppPortfolioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SalesAppPortfolioApp() // Llama al Composable principal de la app
        }
    }
}

@Composable
fun SalesAppPortfolioApp() {
    SalesAppPortfolioTheme {
        val navController = rememberNavController() // Crea el NavController

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            // Llama a tu AppNavHost, que contiene la lógica de navegación
            AppNavHost(navController = navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SalesAppPortfolioApp() // El Preview ahora también usa el NavHost
}
package com.alandiaz.salesappportfolio.ui.theme // PASO 1: Asegúrate que este paquete sea el correcto

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
// Este es un ejemplo estándar. Puedes personalizar las fuentes y estilos.
// Asegúrate de que esta sea una declaración a NIVEL SUPERIOR (directamente en el archivo, no dentro de una clase).
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
// Puedes descomentar y personalizar otros estilos de texto si los necesitas:
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold, // Ejemplo: Cambiado a Bold
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
           // /\* También puedes definir otros estilos como:
            //displayLarge, displayMedium, displaySmall,
   // headlineLarge, headlineMedium, headlineSmall,
    //titleMedium, titleSmall,
    //bodyMedium, bodySmall,
    //labelMedium, labelLarge
    //\*/
)
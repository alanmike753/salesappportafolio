package com.alandiaz.salesappportfolio.utils // Asegúrate que el paquete sea el correcto

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

/**
 * Función de extensión que busca y devuelve la Activity contenedora desde un Context.
 * Es útil para obtener la Activity desde dentro de un Composable.
 */
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
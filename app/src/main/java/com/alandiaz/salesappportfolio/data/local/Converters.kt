package com.alandiaz.salesappportfolio.data.local

import androidx.room.TypeConverter
import java.util.Date

/**
 * Convertidores de tipos para que Room pueda manejar tipos de datos
 * que no son soportados nativamente por SQLite.
 */
class Converters {
    /**
     * Convierte un Timestamp (Long) desde la base de datos a un objeto Date.
     * Room usará este método al leer desde la base de datos.
     * @param value El valor Long (milisegundos) desde la base de datos. Puede ser nulo.
     * @return Un objeto Date o null si el valor de entrada es nulo.
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Convierte un objeto Date a un Timestamp (Long) para guardarlo en la base de datos.
     * Room usará este método al escribir en la base de datos.
     * @param date El objeto Date que se va a convertir. Puede ser nulo.
     * @return Un Long que representa los milisegundos, o null si la fecha de entrada es nula.
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
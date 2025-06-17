package com.alandiaz.salesappportfolio.data.local // Asegúrate que el paquete sea el correcto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa a un usuario en la base de datos local.
 * La información se obtiene de Firebase Authentication.
 *
 * @param userId El ID único del usuario, que será el UID de Firebase. Es la clave primaria.
 * @param email El correo electrónico del usuario.
 * @param displayName El nombre de usuario, que puede ser nulo.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "display_name")
    val displayName: String?
)
package cl.gymtastic.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val passHash: String,
    val nombre: String,
    val rol: String,

    // --- Campos de Suscripción ---
    val planEndMillis: Long? = null,
    val sedeId: Int? = null,
    val sedeName: String? = null,
    val sedeLat: Double? = null,
    val sedeLng: Double? = null,

     //* URI (como String) de la imagen de perfil del usuario,
     //* almacenada internamente en la app.
     //Null si usa la imagen por defecto.
    val avatarUri: String? = null // <-- AÑADIDO

) : Serializable { // Serializable si necesitas pasarlo entre fragments/actividades

    ///** Lógica de negocio para el plan activo */
    val hasActivePlan: Boolean
        get() {
            val end = planEndMillis ?: return false
            return end > System.currentTimeMillis()
        }
}


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

    // --- CAMPOS DE SUSCRIPCIÓN MOVIDOS AQUÍ ---
    // Ya no usamos 'isSubscribed', usamos 'planEndMillis' para saber si está activo
    val planEndMillis: Long? = null,
    val sedeId: Int? = null,
    val sedeName: String? = null,
    val sedeLat: Double? = null, // Room soporta Double (lo guarda como REAL)
    val sedeLng: Double? = null

) : Serializable { // Serializable si necesitas pasarlo entre fragments/actividades

    /**
     * Lógica de negocio (antes en MembershipState).
     * El plan está activo si la fecha de fin es futura.
     * NO necesita @Ignore, Room lo ignora automáticamente.
     */
    val hasActivePlan: Boolean
        get() {
            val end = planEndMillis ?: return false
            return end > System.currentTimeMillis()
        }
}


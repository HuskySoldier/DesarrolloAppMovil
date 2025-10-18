package cl.gymtastic.app.data.local

data class Sede(
    val id: String,
    val nombre: String,
    val lat: Double,
    val lng: Double,
    val direccion: String
)

object SedesRepo {
    val sedes = listOf(
        Sede("stgo-centro", "Santiago Centro", -33.4489, -70.6693, "Av. Principal 123"),
        Sede("providencia", "Providencia", -33.4263, -70.6150, "Av. Nueva 456"),
        Sede("las-condes", "Las Condes", -33.4087, -70.5670, "Calle Firme 789")
    )
}

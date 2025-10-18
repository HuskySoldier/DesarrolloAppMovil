package cl.gymtastic.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val trainerId: Long,
    val fechaHora: Long,
    val estado: String = "pendiente"
)
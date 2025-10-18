package cl.gymtastic.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: Long,
    val timestamp: Long = System.currentTimeMillis(), // check-in
    val checkOutTimestamp: Long? = null               // check-out (opcional)
)

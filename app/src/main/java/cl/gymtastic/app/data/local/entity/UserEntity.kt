package cl.gymtastic.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val email: String,
    val passHash: String,
    val nombre: String,
    val rol: String = "user",
    val createdAt: Long = System.currentTimeMillis()
)
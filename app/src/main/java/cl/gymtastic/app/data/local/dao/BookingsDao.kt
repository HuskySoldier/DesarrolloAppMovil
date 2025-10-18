package cl.gymtastic.app.data.local.dao

import androidx.room.*
import cl.gymtastic.app.data.local.entity.BookingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingsDao {
    @Insert
    suspend fun insert(b: BookingEntity): Long

    @Query("SELECT * FROM bookings WHERE userId = :uid ORDER BY fechaHora DESC")
    fun observeByUser(uid: Long): Flow<List<BookingEntity>>

    @Update
    suspend fun update(b: BookingEntity)

    @Delete
    suspend fun delete(b: BookingEntity)
}
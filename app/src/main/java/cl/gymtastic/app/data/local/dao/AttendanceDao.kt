package cl.gymtastic.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.gymtastic.app.data.local.entity.AttendanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {

    @Insert
    suspend fun insert(reg: AttendanceEntity): Long

    @Query("SELECT * FROM attendance WHERE userId = :userId ORDER BY timestamp DESC")
    fun observeByUser(userId: Long): Flow<List<AttendanceEntity>>

    // 1) obtén el último check-in “abierto”
    @Query("""
        SELECT id FROM attendance
        WHERE userId = :userId AND checkOutTimestamp IS NULL
        ORDER BY timestamp DESC
        LIMIT 1
    """)
    suspend fun findLastOpenAttendanceId(userId: Long): Long?

    // 2) actualiza por id
    @Query("""
        UPDATE attendance
        SET checkOutTimestamp = :checkOut
        WHERE id = :attendanceId
    """)
    suspend fun updateCheckOutById(attendanceId: Long, checkOut: Long)
}

package cl.gymtastic.app.data.repository

import android.content.Context
import cl.gymtastic.app.data.local.db.GymTasticDatabase
import cl.gymtastic.app.data.local.entity.AttendanceEntity

class AttendanceRepository(context: Context) {
    private val db = GymTasticDatabase.get(context)

    fun observe(userId: Long) = db.attendance().observeByUser(userId)

    suspend fun checkIn(userId: Long) {
        db.attendance().insert(
            AttendanceEntity(userId = userId, timestamp = System.currentTimeMillis())
        )
    }

    suspend fun checkOut(userId: Long) {
        val id = db.attendance().findLastOpenAttendanceId(userId)
        if (id != null) {
            db.attendance().updateCheckOutById(id, System.currentTimeMillis())
        }
    }
}


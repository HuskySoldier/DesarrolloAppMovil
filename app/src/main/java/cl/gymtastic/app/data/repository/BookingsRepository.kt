package cl.gymtastic.app.data.repository

import android.content.Context
import cl.gymtastic.app.data.local.db.GymTasticDatabase
import cl.gymtastic.app.data.local.entity.BookingEntity

class BookingsRepository(context: Context) {
    private val db = GymTasticDatabase.get(context)
    fun observeByUser(uid: Long) = db.bookings().observeByUser(uid)
    suspend fun create(uid: Long, trainerId: Long, fechaHora: Long) {
        db.bookings().insert(BookingEntity(userId = uid, trainerId = trainerId, fechaHora = fechaHora))
    }
}
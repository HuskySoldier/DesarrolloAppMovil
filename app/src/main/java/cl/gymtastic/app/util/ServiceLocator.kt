package cl.gymtastic.app.util

import android.content.Context
import cl.gymtastic.app.data.repository.*

object ServiceLocator {
    fun auth(context: Context) = AuthRepository(context)
    fun products(context: Context) = ProductsRepository(context)
    fun cart(context: Context) = CartRepository(context)
    fun attendance(context: Context) = AttendanceRepository(context)
    fun trainers(context: Context) = TrainersRepository(context)
    fun bookings(context: Context) = BookingsRepository(context)
}
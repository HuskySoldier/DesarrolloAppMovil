package cl.gymtastic.app.data.repository

import android.content.Context
import cl.gymtastic.app.data.local.db.GymTasticDatabase

class TrainersRepository(context: Context) {
    private val db = GymTasticDatabase.get(context)
    fun observeAll() = db.trainers().observeAll()
}
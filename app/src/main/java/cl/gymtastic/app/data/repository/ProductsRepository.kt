package cl.gymtastic.app.data.repository

import android.content.Context
import cl.gymtastic.app.data.local.db.GymTasticDatabase

class ProductsRepository(context: Context) {
    private val db = GymTasticDatabase.get(context)
    fun observePlanes() = db.products().observeByTipo("plan")
    fun observeMerch() = db.products().observeByTipo("merch")
    suspend fun getById(id: Long) = db.products().getById(id)
}
package cl.gymtastic.app.data.local

import android.content.Context
import cl.gymtastic.app.data.local.db.GymTasticDatabase

class ProductsRepository(context: Context) {
    private val dao = GymTasticDatabase.get(context).products()

    // ⚡ Para obtener los tipos de producto (plan, merch, etc.)
    suspend fun getTypesById(ids: List<Long>): Map<Long, String> {
        return dao.getByIds(ids).associate { it.id to it.tipo }
    }

    // ⚡ Para obtener los nombres reales de los productos
    suspend fun getNamesById(ids: List<Long>): Map<Long, String> {
        return dao.getNamesByIds(ids).associate { it.id to it.nombre }
    }

    fun observePlanes() = dao.observePlanes()
    fun observeMerch() = dao.observeMerch()


    suspend fun getAll() = dao.getAll()
}

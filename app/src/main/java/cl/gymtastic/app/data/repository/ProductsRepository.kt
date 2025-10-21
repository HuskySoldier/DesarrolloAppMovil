package cl.gymtastic.app.data.local

import android.content.Context
import androidx.room.withTransaction
import cl.gymtastic.app.data.local.dao.ProductStockProjection
import cl.gymtastic.app.data.local.db.GymTasticDatabase
import cl.gymtastic.app.data.local.entity.CartItemEntity

// Excepción para informar faltantes sin tocar el carrito
class InsufficientStockException(
    val shortages: List<Pair<Long /*productId*/, Int /*requested*/>>
) : Exception("Stock insuficiente para algunos productos")

class ProductsRepository(context: Context) {
    private val db = GymTasticDatabase.get(context)
    private val dao = db.products()

    // ⚡ Tipos por id (plan/merch)
    suspend fun getTypesById(ids: List<Long>): Map<Long, String> {
        if (ids.isEmpty()) return emptyMap()
        return dao.getByIds(ids).associate { it.id to it.tipo }
    }

    // ⚡ Nombres por id (para mostrar en UI)
    suspend fun getNamesById(ids: List<Long>): Map<Long, String> {
        if (ids.isEmpty()) return emptyMap()
        return dao.getNamesByIds(ids).associate { it.id to it.nombre }
    }

    // 🌊 Flujos
    fun observePlanes() = dao.observePlanes()
    fun observeMerch() = dao.observeMerch()

    // ✅ (opcional) obtener todo
    suspend fun getAll() = dao.getAll()

    // ⬇️ Stock: lectura directa (útil si quieres mostrar disponibilidades)
    suspend fun getStockByIds(ids: List<Long>): List<ProductStockProjection> {
        return dao.getStockByIds(ids)
    }


    /**
     * Reserva y descuenta stock de productos "merch" en una transacción.
     * - Si alguno no tiene stock suficiente, lanza InsufficientStockException y NO descuenta nada.
     * - Los items "plan" se ignoran aquí (no usan stock).
     */
    suspend fun reserveAndDecrementMerchStock(
        items: List<CartItemEntity>,
        // Si ya tienes un map de tipos, puedes pasarlo para evitar tocar la DB de nuevo.
        typesById: Map<Long, String>? = null
    ) {
        if (items.isEmpty()) return

        // Filtra solo merch; si no te pasan tipos, los resuelve con la DB
        val resolvedTypes = typesById ?: run {
            val ids = items.map { it.productId }.distinct()
            getTypesById(ids)
        }
        val merchItems = items.filter { resolvedTypes[it.productId] == "merch" }
        if (merchItems.isEmpty()) return

        db.withTransaction {
            // (Opcional) Lectura previa — no estrictamente necesaria, `tryDecrementStock` ya valida.
            // La mantenemos para poder construir un mensaje mejor si falla.
            val ids = merchItems.map { it.productId }.distinct()
            val stockMap = dao.getStockByIds(ids).associate { it.id to (it.stock ?: Int.MAX_VALUE) }

            val shortages = mutableListOf<Pair<Long, Int>>()

            merchItems.forEach { ci ->
                // Si no tiene stock definido, tratamos como “sin control” (no bloquea)
                val current = stockMap[ci.productId]
                if (current != null) {
                    val updated = dao.tryDecrementStock(ci.productId, ci.qty)
                    if (updated == 0) {
                        shortages += (ci.productId to ci.qty)
                    }
                }
            }

            if (shortages.isNotEmpty()) {
                // Cualquier fallo aborta la transacción
                throw InsufficientStockException(shortages)
            }
        }
    }
}

package cl.gymtastic.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cl.gymtastic.app.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("SELECT * FROM products")
    suspend fun getAll(): List<ProductEntity>   // 👈 agrega esto

    @Query("SELECT COUNT(*) FROM products")
    suspend fun count(): Int

    @Query("SELECT id, nombre FROM products WHERE id IN (:ids)")
    suspend fun getNamesByIds(ids: List<Long>): List<ProductNameProjection>

    @Query("SELECT * FROM products WHERE id IN (:ids)")
    suspend fun getByIds(ids: List<Long>): List<ProductEntity>


    @Query("SELECT * FROM products WHERE tipo = 'plan' ORDER BY precio ASC")
    fun observePlanes(): kotlinx.coroutines.flow.Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE tipo = 'merch' ORDER BY nombre ASC")
    fun observeMerch(): kotlinx.coroutines.flow.Flow<List<ProductEntity>>
}

data class ProductNameProjection(
    val id: Long,
    val nombre: String
)

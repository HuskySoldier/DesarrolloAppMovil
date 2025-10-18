package cl.gymtastic.app.data.local.dao

import androidx.room.*
import cl.gymtastic.app.data.local.entity.TrainerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<TrainerEntity>)

    @Query("SELECT * FROM trainers")
    fun observeAll(): Flow<List<TrainerEntity>>
}
package cl.gymtastic.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cl.gymtastic.app.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsersDao {
    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE) // Ignorar si el email ya existe
    suspend fun insert(user: UserEntity): Long // Devuelve Long (-1 si falla/ignora)

    /** Actualiza el estado de suscripción */
    @Query("UPDATE users SET planEndMillis=:planEndMillis, sedeId=:sedeId, sedeName=:sedeName, sedeLat=:sedeLat, sedeLng=:sedeLng WHERE email = :email")
    suspend fun updateSubscription(email: String, planEndMillis: Long?, sedeId: Int?, sedeName: String?, sedeLat: Double?, sedeLng: Double?)

    /** Observa un usuario por email */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun observeByEmail(email: String): Flow<UserEntity?>

    // --- FUNCIONES NUEVAS PARA ADMIN ---

    /** Observa todos los usuarios EXCEPTO uno específico (el admin actual) */
    @Query("SELECT * FROM users WHERE email != :excludeEmail ORDER BY email ASC")
    fun observeAllExcept(excludeEmail: String): Flow<List<UserEntity>>

    /** Actualiza la contraseña hash de un usuario */
    @Query("UPDATE users SET passHash = :newPassHash WHERE email = :email")
    suspend fun updatePasswordHash(email: String, newPassHash: String): Int // Devuelve filas afectadas

    /** Actualiza un usuario completo (útil si añades más campos editables como rol) */
    @Update
    suspend fun update(user: UserEntity): Int // Devuelve filas afectadas

    /** Elimina un usuario por su email */
    @Query("DELETE FROM users WHERE email = :email")
    suspend fun deleteByEmail(email: String): Int // Devuelve filas afectadas

    /** Elimina un usuario usando la entidad (alternativa) */
    @Delete
    suspend fun delete(user: UserEntity): Int // Devuelve filas afectadas
}


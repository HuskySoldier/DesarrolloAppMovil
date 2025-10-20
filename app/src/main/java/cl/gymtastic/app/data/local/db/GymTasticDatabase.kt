package cl.gymtastic.app.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.gymtastic.app.data.local.dao.*
import cl.gymtastic.app.data.local.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class, ProductEntity::class, CartItemEntity::class,
        AttendanceEntity::class, TrainerEntity::class, BookingEntity::class
    ],
    version = 3, // nos quedamos en 3
    exportSchema = false
)
abstract class GymTasticDatabase : RoomDatabase() {
    abstract fun users(): UsersDao
    abstract fun products(): ProductsDao
    abstract fun cart(): CartDao
    abstract fun attendance(): AttendanceDao
    abstract fun trainers(): TrainersDao
    abstract fun bookings(): BookingsDao

    companion object {
        @Volatile private var INSTANCE: GymTasticDatabase? = null

        // 1 -> 2: columna nueva en attendance
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE attendance ADD COLUMN checkOutTimestamp INTEGER")
            }
        }

        // 2 -> 3: sin cambios de esquema (no-op)
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) { /* no-op */ }
        }

        fun get(context: Context): GymTasticDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GymTasticDatabase::class.java,
                    "gymtastic.db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    // .fallbackToDestructiveMigration() // solo si quieres limpiar en debug
                    .build()

                INSTANCE = instance

                // ✅ Seed POST-build, sin llamar recursivamente a get()
                CoroutineScope(Dispatchers.IO).launch {
                    seedIfNeeded(instance)
                }

                instance
            }
        }

        private suspend fun seedIfNeeded(db: GymTasticDatabase) {
            // Usuarios
            val userCount = try { db.users().count() } catch (_: Exception) { 0 }
            if (userCount == 0) {
                db.users().insert(
                    UserEntity(
                        // normalizado en minúsculas
                        email = "admin@gymtastic.cl",
                        // mismo método que usa AuthRepository (hashCode de String.trim())
                        passHash = "admin123".trim().hashCode().toString(),
                        nombre = "Administrador",
                        rol = "admin"
                    )
                )
            }

            // Productos
            val products = try { db.products().getAll() } catch (_: Exception) { emptyList() }
            if (products.isEmpty()) {
                db.products().insertAll(
                    listOf(
                        ProductEntity(nombre = "Plan Mensual", precio = 19990.0, tipo = "plan"),
                        ProductEntity(nombre = "Plan Trimestral", precio = 54990.0, tipo = "plan"),
                        ProductEntity(nombre = "Polera Gym", precio = 12990.0, tipo = "merch", stock = 50),
                        ProductEntity(nombre = "Botella", precio = 6990.0, tipo = "merch", stock = 80)
                    )
                )
            }

            // Trainers
            val trainersCount = try { db.trainers().count() } catch (_: Exception) { 0 }
            if (trainersCount == 0) {
                db.trainers().insertAll(
                    listOf(
                        TrainerEntity(
                            nombre = "Ana Pérez",
                            fono = "+56911111111",
                            email = "ana@gymtastic.cl",
                            especialidad = "Funcional"
                        ),
                        TrainerEntity(
                            nombre = "Luis Gómez",
                            fono = "+56922222222",
                            email = "luis@gymtastic.cl",
                            especialidad = "Hipertrofia"
                        )
                    )
                )
            }
        }
    }
}


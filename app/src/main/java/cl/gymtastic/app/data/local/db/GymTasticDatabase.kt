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
    // Versión 8 para incluir la nueva migración
    version = 8,
    exportSchema = false
)
abstract class GymTasticDatabase : RoomDatabase() {
    // --- DAOs abstractos ---
    abstract fun users(): UsersDao
    abstract fun products(): ProductsDao
    abstract fun cart(): CartDao
    abstract fun attendance(): AttendanceDao
    abstract fun trainers(): TrainersDao
    abstract fun bookings(): BookingsDao

    companion object {
        @Volatile private var INSTANCE: GymTasticDatabase? = null

        // --- Migraciones Existentes ---
        private val MIGRATION_1_2 = object : Migration(1, 2) { /* ... */
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE attendance ADD COLUMN checkOutTimestamp INTEGER")
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) { /* ... */
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE products ADD COLUMN img TEXT")
                db.execSQL("ALTER TABLE trainers ADD COLUMN img TEXT")
            }
        }
        private val MIGRATION_3_4 = object : Migration(3, 4) { /* ... */
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN planEndMillis INTEGER")
                db.execSQL("ALTER TABLE users ADD COLUMN sedeId INTEGER")
                db.execSQL("ALTER TABLE users ADD COLUMN sedeName TEXT")
                db.execSQL("ALTER TABLE users ADD COLUMN sedeLat REAL")
                db.execSQL("ALTER TABLE users ADD COLUMN sedeLng REAL")
            }
        }
        private val MIGRATION_4_5 = object : Migration(4, 5) { /* ... */ // userId -> userEmail en attendance
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE attendance_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, userEmail TEXT NOT NULL, timestamp INTEGER NOT NULL, checkOutTimestamp INTEGER)")
                // Asumiendo que UserEntity PK es email y attendance.userId contenía el email
                db.execSQL("INSERT INTO attendance_new (id, userEmail, timestamp, checkOutTimestamp) SELECT id, userId, timestamp, checkOutTimestamp FROM attendance")
                db.execSQL("DROP TABLE attendance")
                db.execSQL("ALTER TABLE attendance_new RENAME TO attendance")
            }
        }
        private val MIGRATION_5_6 = object : Migration(5, 6) { /* ... */ // Vacía
            override fun migrate(db: SupportSQLiteDatabase) { }
        }
        private val MIGRATION_6_7 = object : Migration(6, 7) { /* ... */ // Añade avatarUri a users
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN avatarUri TEXT")
            }
        }

        // --- MIGRACIÓN 7 -> 8: Eliminar userId de bookings (CORREGIDA) ---
        private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 1. Crear tabla temporal SIN la columna 'userId'
                db.execSQL("""
                    CREATE TABLE bookings_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userEmail TEXT NOT NULL,
                        trainerId INTEGER NOT NULL,
                        fechaHora INTEGER NOT NULL,
                        estado TEXT NOT NULL DEFAULT 'pendiente'
                    )
                """)

                // 2. Copiar datos: Selecciona userId de la tabla vieja y busca el userEmail en 'users'
                //    NOTA: Esto asume que la PK de 'users' es 'email'. Si no lo es, ajusta la JOIN.
                //    Los bookings cuyo userId no corresponda a un email en users se perderán.
                db.execSQL("""
                    INSERT INTO bookings_new (id, userEmail, trainerId, fechaHora, estado)
                    SELECT b.id, u.email, b.trainerId, b.fechaHora, b.estado
                    FROM bookings AS b
                    INNER JOIN users AS u ON b.userId = u.email -- O la columna PK correcta si no es email
                """)
                // Si 'userId' en la tabla 'bookings' antigua contenía directamente el email, usa esta query:
                /*
                db.execSQL("""
                    INSERT INTO bookings_new (id, userEmail, trainerId, fechaHora, estado)
                    SELECT id, userId, trainerId, fechaHora, estado
                    FROM bookings
                """)
                */

                // 3. Eliminar la tabla vieja
                db.execSQL("DROP TABLE bookings")

                // 4. Renombrar la tabla nueva
                db.execSQL("ALTER TABLE bookings_new RENAME TO bookings")
            }
        }
        // --- FIN MIGRACIÓN CORREGIDA ---


        fun get(context: Context): GymTasticDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GymTasticDatabase::class.java,
                    "gymtastic.db"
                )
                    // Añadir TODAS las migraciones en orden, incluyendo la 7_8
                    .addMigrations(
                        MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4,
                        MIGRATION_4_5, MIGRATION_5_6, MIGRATION_6_7,
                        MIGRATION_7_8 // <-- Incluida
                    )
                    // .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                // Seed POST-build
                CoroutineScope(Dispatchers.IO).launch {
                    seedIfNeeded(instance, context.applicationContext)
                }

                instance
            }
        }

        // --- Función Seed (Sin cambios necesarios aquí) ---
        private suspend fun seedIfNeeded(db: GymTasticDatabase, context: Context) { /* ... */ }
    }
}


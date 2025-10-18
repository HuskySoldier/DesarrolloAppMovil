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
    version = 2, // ⬅️ subimos la versión porque cambió el esquema (nueva columna)
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

        // ✅ Migración 1→2: agrega columna checkOutTimestamp (INTEGER, admite NULL)
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE attendance ADD COLUMN checkOutTimestamp INTEGER")
            }
        }

        fun get(context: Context): GymTasticDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, GymTasticDatabase::class.java, "gymtastic.db")
                    .addMigrations(MIGRATION_1_2)           // ⬅️ aplica migración sin perder datos
                    .fallbackToDestructiveMigration()       // ⬅️ útil en desarrollo si cambias más cosas
                    .addCallback(object: Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                // Seed inicial
                                val daoUsers = get(context).users()
                                if (daoUsers.count() == 0) {
                                    daoUsers.insert(
                                        UserEntity(
                                            email = "admin@gymtastic.cl",
                                            passHash = "admin123".hashCode().toString(),
                                            nombre = "Administrador",
                                            rol = "admin"
                                        )
                                    )
                                }

                                val daoProducts = get(context).products()
                                daoProducts.insertAll(
                                    listOf(
                                        ProductEntity(nombre="Plan Mensual", precio=19990.0, tipo="plan"),
                                        ProductEntity(nombre="Plan Trimestral", precio=54990.0, tipo="plan"),
                                        ProductEntity(nombre="Polera Gym", precio=12990.0, tipo="merch", stock=50),
                                        ProductEntity(nombre="Botella", precio=6990.0, tipo="merch", stock=80)
                                    )
                                )

                                val daoTrainers = get(context).trainers()
                                daoTrainers.insertAll(
                                    listOf(
                                        TrainerEntity(nombre="Ana Pérez", fono="+56911111111", email="ana@gymtastic.cl", especialidad="Funcional"),
                                        TrainerEntity(nombre="Luis Gómez", fono="+56922222222", email="luis@gymtastic.cl", especialidad="Hipertrofia")
                                    )
                                )
                            }
                        }
                    })
                    .build().also { INSTANCE = it }
            }
        }
    }
}

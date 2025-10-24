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
    // --- ❗️ VERSIÓN INCREMENTADA A 6 ---
    version = 6,
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

        // --- Migraciones Existentes ---
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE attendance ADD COLUMN checkOutTimestamp INTEGER")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE products ADD COLUMN img TEXT")
                db.execSQL("ALTER TABLE trainers ADD COLUMN img TEXT")
            }
        }

        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN planEndMillis INTEGER")
                db.execSQL("ALTER TABLE users ADD COLUMN sedeId INTEGER")
                db.execSQL("ALTER TABLE users ADD COLUMN sedeName TEXT")
                db.execSQL("ALTER TABLE users ADD COLUMN sedeLat REAL")
                db.execSQL("ALTER TABLE users ADD COLUMN sedeLng REAL")
            }
        }

        // --- ❗️ NUEVA MIGRACIÓN 4 -> 5: Cambiar userId por userEmail en attendance ---
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // 1. Crear tabla temporal con la nueva estructura (userEmail TEXT)
                db.execSQL("""
                    CREATE TABLE attendance_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 
                        userEmail TEXT NOT NULL, 
                        timestamp INTEGER NOT NULL, 
                        checkOutTimestamp INTEGER
                    )
                """)

                // 2. Copiar datos de la tabla vieja a la nueva, buscando el email en 'users'
                //    NOTA: Si un userId de attendance no existe en users, ese registro se perderá.
                //    Usamos COALESCE para manejar el caso donde checkOutTimestamp es NULL.
                db.execSQL("""
                    INSERT INTO attendance_new (id, userEmail, timestamp, checkOutTimestamp)
                    SELECT att.id, usr.email, att.timestamp, att.checkOutTimestamp
                    FROM attendance AS att
                    INNER JOIN users AS usr ON att.userId = usr.id 
                """) // Ajusta 'usr.id' si tu PK en UserEntity NO es 'id' (si es 'email', esta join no funciona directamente así)
                // --- AJUSTE SI USER PK ES EMAIL ---
                // Si la clave primaria de UserEntity es 'email' y 'userId' en la tabla 'attendance' antigua
                // realmente contenía un email (o algo que no se puede unir directamente a un ID numérico),
                // necesitarás ajustar la lógica de copia.
                // SI 'userId' contenía un email, la query sería más simple:
                /*
                 db.execSQL("""
                    INSERT INTO attendance_new (id, userEmail, timestamp, checkOutTimestamp)
                    SELECT id, userId, timestamp, checkOutTimestamp
                    FROM attendance
                 """)
                 // Y tendrías que haber renombrado userId a userEmail directamente si SQLite lo permite,
                 // o crear la tabla nueva, copiar, borrar vieja, renombrar nueva.
                 */
                // Asumiremos que UserEntity SÍ tenía un ID numérico 'id' y AttendanceEntity tenía 'userId' como FK a ese 'id'.
                // Si UserEntity *siempre* usó 'email' como PK y AttendanceEntity *siempre* tuvo 'userId' numérico,
                // entonces hay una inconsistencia fundamental en tu esquema anterior.
                // La query con INNER JOIN es la forma estándar asumiendo FK numérica.

                // 3. Eliminar la tabla vieja
                db.execSQL("DROP TABLE attendance")

                // 4. Renombrar la tabla nueva
                db.execSQL("ALTER TABLE attendance_new RENAME TO attendance")
            }
        }

        // --- ❗️ NUEVA MIGRACIÓN 5 -> 6: ---
        // Aquí necesitarías la migración para cambiar la FK en AttendanceEntity
        // Si asumimos que en la v5 ya tenías userEmail TEXT NOT NULL:
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // No hay cambios de esquema si v5 ya tenía la estructura correcta
                // pero incrementamos versión por si acaso o para futuras modificaciones.
                // Si v5 NO tenía la estructura correcta, la migración MIGRATION_4_5 debería ser MIGRATION_5_6.
                // Por simplicidad, asumiré que MIGRATION_4_5 se aplica entre la versión que TENÍAS y la NUEVA versión 6.
                // Reajusta los números si es necesario (ej. si vienes de la v4 directo a la v6).
            }
        }


        fun get(context: Context): GymTasticDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GymTasticDatabase::class.java,
                    "gymtastic.db"
                )
                    // --- ❗️ AÑADIR NUEVA MIGRACIÓN ---
                    // Asegúrate que los números de versión sean consecutivos y correctos
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                    // .fallbackToDestructiveMigration() // solo si quieres limpiar en debug
                    .build()

                INSTANCE = instance

                // Seed POST-build
                CoroutineScope(Dispatchers.IO).launch {
                    seedIfNeeded(instance, context.applicationContext)
                }

                instance
            }
        }

        // --- Función Seed (Sin cambios respecto a la versión anterior) ---
        private suspend fun seedIfNeeded(db: GymTasticDatabase, context: Context) {
            // Usuarios
            val userCount = try { db.users().count() } catch (_: Exception) { 0 }
            if (userCount == 0) {
                db.users().insert(
                    UserEntity(
                        email = "admin@gymtastic.cl",
                        passHash = "admin123".trim().hashCode().toString(),
                        nombre = "Administrador",
                        rol = "admin",
                        planEndMillis = null, sedeId = null, sedeName = null, sedeLat = null, sedeLng = null
                    )
                )
                db.users().insert(
                    UserEntity(
                        email = "test@gymtastic.cl",
                        passHash = "test1234".trim().hashCode().toString(),
                        nombre = "Usuario Test",
                        rol = "user",
                        planEndMillis = null, sedeId = null, sedeName = null, sedeLat = null, sedeLng = null
                    )
                )
            }

            // PackageName para URIs de recursos
            val packageName = context.packageName

            // Productos
            val products = try { db.products().getAll() } catch (_: Exception) { emptyList() }
            if (products.isEmpty()) {
                val poleraImgUri = "android.resource://$packageName/drawable/polera_gym"
                val botellaImgUri = "android.resource://$packageName/drawable/botella_agua"
                db.products().insertAll(
                    listOf(
                        ProductEntity(nombre = "Plan Mensual", precio = 19990.0, tipo = "plan"),
                        ProductEntity(nombre = "Plan Trimestral", precio = 54990.0, tipo = "plan"),
                        ProductEntity(nombre = "Polera Gym", precio = 12990.0, tipo = "merch", stock =90, img = poleraImgUri),
                        ProductEntity(nombre = "Botella", precio = 6990.0, tipo = "merch", stock = 80, img = botellaImgUri)
                    )
                )
            }

            // Trainers
            val trainersCount = try { db.trainers().count() } catch (_: Exception) { 0 }
            if (trainersCount == 0) {
                val anaImgUri = "android.resource://$packageName/drawable/trainer_ana"
                val luisImgUri = "android.resource://$packageName/drawable/trainer_luis"
                db.trainers().insertAll(
                    listOf(
                        TrainerEntity(
                            nombre = "Ana Pérez",
                            fono = "+56911111111",
                            email = "ana@gymtastic.cl",
                            especialidad = "Funcional",
                            img = anaImgUri
                        ),
                        TrainerEntity(
                            nombre = "Luis Gómez",
                            fono = "+56922222222",
                            email = "luis@gymtastic.cl",
                            especialidad = "Hipertrofia",
                            img = luisImgUri
                        )
                    )
                )
            }
        }
    }
}
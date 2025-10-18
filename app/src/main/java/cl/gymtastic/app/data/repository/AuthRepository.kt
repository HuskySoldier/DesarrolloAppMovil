package cl.gymtastic.app.data.repository

import android.content.Context
import cl.gymtastic.app.data.datastore.SessionPrefs
import cl.gymtastic.app.data.local.db.GymTasticDatabase
import cl.gymtastic.app.data.local.entity.UserEntity

class AuthRepository(private val context: Context) {
    private val db = GymTasticDatabase.get(context)
    private val prefs = SessionPrefs(context)

    suspend fun register(email: String, password: String, nombre: String): Boolean {
        val existing = db.users().findByEmail(email)
        if (existing != null) return false
        db.users().insert(
            UserEntity(
                email = email,
                passHash = password.hashCode().toString(),
                nombre = nombre
            )
        )
        return true
    }

    suspend fun login(email: String, password: String): Boolean {
        val u = db.users().findByEmail(email) ?: return false
        if (u.passHash == password.hashCode().toString()) {
            prefs.setUser(u.id.toInt(), "local-token-${u.id}")
            return true
        }
        return false
    }

    suspend fun logout() {
        // ✅ Limpiar los datos de sesión almacenados
        prefs.clear()
    }

    fun prefs() = prefs
}

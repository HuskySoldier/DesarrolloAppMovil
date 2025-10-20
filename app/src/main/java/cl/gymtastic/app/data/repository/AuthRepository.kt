package cl.gymtastic.app.data.repository

import android.content.Context
import cl.gymtastic.app.data.datastore.SessionPrefs
import cl.gymtastic.app.data.local.db.GymTasticDatabase
import cl.gymtastic.app.data.local.entity.UserEntity

class AuthRepository(private val context: Context) {
    private val db = GymTasticDatabase.get(context)
    private val prefs = SessionPrefs(context)

    // funciones auxiliares
    private fun normEmail(raw: String) = raw.trim().lowercase()
    private fun hash(raw: String) = raw.trim().hashCode().toString()

    suspend fun register(email: String, password: String, nombre: String): Boolean {
        val e = normEmail(email)
        val existing = db.users().findByEmail(e)
        if (existing != null) return false
        db.users().insert(
            UserEntity(
                email = e,
                passHash = hash(password),
                nombre = nombre,
                rol = "user"
            )
        )
        return true
    }

    suspend fun login(email: String, password: String): Boolean {
        val e = normEmail(email)
        val u = db.users().findByEmail(e) ?: return false
        if (u.passHash == hash(password)) {
            prefs.setUser(u.id.toInt(), "local-token-${u.id}")
            return true
        }
        return false
    }

    suspend fun logout() {
        prefs.clear()
    }

    fun prefs() = prefs
}

package cl.gymtastic.app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "membership_prefs")

object MembershipPrefs {
    private val KEY_ACTIVE = booleanPreferencesKey("has_active_plan")
    private val KEY_SEDE_ID = stringPreferencesKey("sede_id")
    private val KEY_SEDE_NAME = stringPreferencesKey("sede_name")
    private val KEY_SEDE_LAT = doublePreferencesKey("sede_lat")
    private val KEY_SEDE_LNG = doublePreferencesKey("sede_lng")

    data class State(
        val hasActivePlan: Boolean = false,
        val sedeId: String? = null,
        val sedeName: String? = null,
        val sedeLat: Double? = null,
        val sedeLng: Double? = null
    )

    fun observe(ctx: Context): Flow<State> =
        ctx.dataStore.data.map { p ->
            State(
                hasActivePlan = p[KEY_ACTIVE] ?: false,
                sedeId = p[KEY_SEDE_ID],
                sedeName = p[KEY_SEDE_NAME],
                sedeLat = p[KEY_SEDE_LAT],
                sedeLng = p[KEY_SEDE_LNG]
            )
        }

    suspend fun setActiveWithSede(
        ctx: Context,
        id: String, name: String, lat: Double, lng: Double
    ) {
        ctx.dataStore.edit { p ->
            p[KEY_ACTIVE] = true
            p[KEY_SEDE_ID] = id
            p[KEY_SEDE_NAME] = name
            p[KEY_SEDE_LAT] = lat
            p[KEY_SEDE_LNG] = lng
        }
    }

    suspend fun clear(ctx: Context) {
        ctx.dataStore.edit { it.clear() }
    }
}

package cl.gymtastic.app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

object MembershipPrefs {
    private val KEY_ACTIVE = booleanPreferencesKey("membership_active")
    private val KEY_SEDE_ID = intPreferencesKey("membership_sede_id")
    private val KEY_SEDE_NAME = stringPreferencesKey("membership_sede_name")
    private val KEY_SEDE_LAT = doublePreferencesKey("membership_sede_lat")
    private val KEY_SEDE_LNG = doublePreferencesKey("membership_sede_lng")

    private val KEY_PLAN_END = longPreferencesKey("membership_plan_end_millis")

    data class State(
        val hasActivePlan: Boolean = false,
        val sedeId: Int? = null,
        val sedeName: String? = null,
        val sedeLat: Double? = null,
        val sedeLng: Double? = null,
        val planEndMillis: Long? = null
    ) {
        fun remainingDays(now: Long = System.currentTimeMillis()): Long? =
            planEndMillis?.let { end ->
                val diff = end - now
                if (diff <= 0) 0 else TimeUnit.MILLISECONDS.toDays(diff)
            }
    }

    fun observe(ctx: Context): Flow<State> =
        ctx.dataStore.data.map { p ->
            State(
                hasActivePlan = p[KEY_ACTIVE] ?: false,
                sedeId = p[KEY_SEDE_ID] as Int?,
                sedeName = p[KEY_SEDE_NAME],
                sedeLat = p[KEY_SEDE_LAT],
                sedeLng = p[KEY_SEDE_LNG],
                planEndMillis = p[KEY_PLAN_END]
            )
        }

    // Llamar al contratar: activa plan + fija fecha de fin
    suspend fun setActiveWithSede(
        ctx: Context,
        id: Int,
        name: String,
        lat: Double,
        lng: Double,
        planEndMillis: Long // 👈 fecha de término calculada por el pago
    ) {
        ctx.dataStore.edit { p ->
            p[KEY_ACTIVE] = true
            p[KEY_SEDE_ID] = id
            p[KEY_SEDE_NAME] = name
            p[KEY_SEDE_LAT] = lat
            p[KEY_SEDE_LNG] = lng
            p[KEY_PLAN_END] = planEndMillis
        }
    }

    // Helper para política de compra
    suspend fun canPurchaseNewPlan(ctx: Context, thresholdDays: Long = 3): Boolean {
        val st = observe(ctx)
            .map { it }
            .firstOrNull() ?: State()
        if (!st.hasActivePlan) return true
        val rem = st.remainingDays() ?: return true
        return rem <= thresholdDays
    }

    suspend fun clear(ctx: Context) {
        ctx.dataStore.edit { it.clear() }
    }
}

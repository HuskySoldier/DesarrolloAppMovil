package cl.gymtastic.app

import android.app.Application
import androidx.work.*
import cl.gymtastic.app.work.ReminderWorker
import java.util.concurrent.TimeUnit

class GymTasticApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Daily reminder at ~8 hours interval as an example (flexible demo)
        val request = PeriodicWorkRequestBuilder<ReminderWorker>(24, TimeUnit.HOURS)
            .setConstraints(Constraints.Builder().setRequiresBatteryNotLow(false).build())
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
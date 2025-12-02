package com.example.recipeapp.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.recipeapp.data.local.AppDatabase


class CleanupWorker(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        return try {
            // Clear all "has item" flags daily so stale ingredient/shopping states do not linger.
            val db = AppDatabase.getInstance(applicationContext)
            db.ingredientDao().clearAllChecks()
            db.shoppingListDao().clearChecks()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}

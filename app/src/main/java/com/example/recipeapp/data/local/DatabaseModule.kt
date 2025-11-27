package com.example.recipeapp.data.local

import android.content.Context
import androidx.room.Room

object DatabaseModule {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun provideDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "recipe_app_db"
            ).build().also { INSTANCE = it }
        }
    }
}

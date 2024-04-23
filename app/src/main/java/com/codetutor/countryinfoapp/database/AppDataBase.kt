package com.codetutor.countryinfoapp.database

import CountryEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CountryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countryDao(): CountryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "country_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
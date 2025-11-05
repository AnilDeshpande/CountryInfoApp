package com.codetutor.countryinfoapp.database.appdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.database.converters.Converters
import com.codetutor.countryinfoapp.database.dao.CountryDao

@Database(entities = [Country::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(), DatabaseProvider {
    abstract override fun countryDao(): CountryDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseProvider? = null

        fun getDatabase(context: Context): DatabaseProvider {
            return INSTANCE ?: synchronized(this) {
                val instance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "country_database",
                    ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

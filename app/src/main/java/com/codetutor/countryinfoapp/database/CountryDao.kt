package com.codetutor.countryinfoapp.database

import CountryEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CountryDao {
    @Query("SELECT * FROM CountryEntity")
    suspend fun getAllCountries(): List<CountryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryEntity>)
}
package com.codetutor.countryinfoapp.database

import CountryEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codetutor.countryinfoapp.data.Country

@Dao
interface CountryDao {
    @Query("SELECT * FROM Country")
    suspend fun getAllCountries(): List<Country>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<Country>)
}
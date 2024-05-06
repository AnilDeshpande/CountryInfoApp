package com.codetutor.countryinfoapp.database

import CountryEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.codetutor.countryinfoapp.data.Country

@Dao
interface CountryDao {
    @Query("SELECT * FROM Country")
    suspend fun getAllCountries(): List<Country>

    @Query("SELECT * FROM Country WHERE continents LIKE :continent")
    suspend fun getCountriesByContinent(continent: String): List<Country>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<Country>)

    @Delete
    suspend fun delete(country: Country)

    @Query("Update Country set capital = :capital where id = :id")
    suspend fun updateCapital(capital: List<String>, id: Int): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateCountry(country: Country): Int
}
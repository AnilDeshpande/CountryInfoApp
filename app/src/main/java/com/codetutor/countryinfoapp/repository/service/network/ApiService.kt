package com.codetutor.countryinfoapp.repository.service.network


import com.codetutor.countryinfoapp.data.Country
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v3.1/all")
    suspend fun getAllCountries(@Query("fields") fields: String = "name,capital,region,subregion,currencies,idd,tld,flags,flag,car"): Response<MutableList<Country>>
}
package com.codetutor.countryinfoapp.database.appdb

import com.codetutor.countryinfoapp.database.dao.CountryDao

interface DatabaseProvider {
    fun countryDao(): CountryDao
}
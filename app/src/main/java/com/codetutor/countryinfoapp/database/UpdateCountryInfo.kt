package com.codetutor.countryinfoapp.database

import com.codetutor.countryinfoapp.data.Country

data class UpdateCountryInfo(val currentCountry: Country,val newCapital: String) {
}

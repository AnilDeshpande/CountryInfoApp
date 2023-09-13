package com.codetutor.countryinfoapp.data

import com.codetutor.countryinfoapp.R


fun getCountryList(): MutableList<CountryInfo> {
    val countryList = mutableListOf<CountryInfo>()
    countryList.add(CountryInfo(
        R.drawable.`in`,
        "India",
        "New Delhi",
        "Republic of India",
        "Asia","South Asia",
        "₹",
        "Indian Rupee",
        "+91",
        ".in"));

    countryList.add(CountryInfo(
        R.drawable.us,
        "United States",
        "Washington, D.C.",
        "United States of America",
        "Americas","North America",
        "$",
        "United States dollar",
        "+1",
        ".us"))

    countryList.add(CountryInfo(
        R.drawable.bd,
        "Bangladesh",
        "Dhaka",
        "People's Republic of Bangladesh",
        "Asia","South Asia",
        "৳",
        "Bangladeshi taka",
        "+880",
        ".bd"))

    return countryList
}
package com.codetutor.countryinfoapp.util

import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterByDriveSide
import com.codetutor.countryinfoapp.repository.FilterCriteria

interface FilterCriteriaFactory {
    fun createFilterCriteria(filterKey: String): FilterCriteria
}
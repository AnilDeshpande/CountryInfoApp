package com.codetutor.countryinfoapp.util

import com.codetutor.countryinfoapp.repository.FilterByContinent
import com.codetutor.countryinfoapp.repository.FilterCriteria

class ContinentFilterFactory : FilterCriteriaFactory {
    override fun createFilterCriteria(filterKey: String): FilterCriteria {
        return FilterByContinent(filterKey)
    }
}
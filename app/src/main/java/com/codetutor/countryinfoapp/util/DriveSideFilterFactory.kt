package com.codetutor.countryinfoapp.util

import com.codetutor.countryinfoapp.repository.FilterByDriveSide
import com.codetutor.countryinfoapp.repository.FilterCriteria

class DriveSideFilterFactory : FilterCriteriaFactory {
    override fun createFilterCriteria(filterKey: String): FilterCriteria {
        return FilterByDriveSide(filterKey)
    }
}
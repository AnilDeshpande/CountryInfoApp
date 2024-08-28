package com.codetutor.countryinfoapp.util

object FilterCriteriaFactoryProvider {
    private val factories = mapOf(
        "Continent" to ContinentFilterFactory(),
        "Drive Side" to DriveSideFilterFactory()
    )

    fun getFactory(selectedFilterValue: String): FilterCriteriaFactory? {
        return factories[selectedFilterValue]
    }
}
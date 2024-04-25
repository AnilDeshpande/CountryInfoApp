import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.codetutor.countryinfoapp.data.Country
import com.codetutor.countryinfoapp.data.Translations
import com.codetutor.countryinfoapp.database.TranslationsConverter

@Entity
data class CountryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val altSpellings: String? = null, // Converted to String
    val area: Double? = null,
    val capital: String? = null, // Converted to String
    val cca2: String? = null,
    val cca3: String? = null,
    val ccn3: String? = null,
    val continents: String? = null, // Converted to String
    val currencies: String? = null, // Converted to String
    val flag: String? = null,
    val independent: Boolean? = null,
    val landlocked: Boolean? = null,
    val languages: String? = null, // Converted to String
    val latlng: String? = null, // Converted to String
    val name: String? = null,
    val population: Int? = null,
    val region: String? = null,
    val startOfWeek: String? = null,
    val status: String? = null,
    val subregion: String? = null,
    val timezones: String? = null, // Converted to String
    val tld: String? = null, // Converted to String
    @TypeConverters(TranslationsConverter::class)
    val translations: Translations? = null,
    val unMember: Boolean? = null
)

fun countryToEntity(country: Country): CountryEntity {
    return CountryEntity(
        altSpellings = country.altSpellings?.joinToString(),
        area = country.area,
        capital = country.capital?.joinToString(),
        cca2 = country.cca2,
        cca3 = country.cca3,
        ccn3 = country.ccn3,
        continents = country.continents?.joinToString(),
        currencies = country.currencies?.keys?.joinToString(),
        flag = country.flag,
        independent = country.independent,
        landlocked = country.landlocked,
        languages = country.languages?.nor,
        latlng = country.latlng?.joinToString(),
        name = country.name?.common,
        population = country.population,
        region = country.region,
        startOfWeek = country.startOfWeek,
        status = country.status,
        subregion = country.subregion,
        timezones = country.timezones?.joinToString(),
        tld = country.tld?.joinToString(),
        //translations = country.translations,
        unMember = country.unMember
    )
}



package com.codetutor.countryinfoapp.data

import kotlinx.serialization.Serializable

@Serializable
data class Translations(
    val ara: Ara? = null,
    val bre: Bre? = null,
    val ces: Ces? = null,
    val cym: Cym? = null,
    val deu: Deu? = null,
    val est: Est? = null,
    val fin: Fin? = null,
    val fra: Fra? = null,
    val hrv: Hrv? = null,
    val hun: Hun? = null,
    val ita: Ita? = null,
    val jpn: Jpn? = null,
    val kor: Kor? = null,
    val nld: Nld? = null,
    val per: Per? = null,
    val pol: Pol? = null,
    val por: Por? = null,
    val rus: Rus? = null,
    val slk: Slk? = null,
    val spa: Spa? = null,
    val srp: Srp? = null,
    val swe: Swe? = null,
    val tur: Tur? = null,
    val urd: Urd? = null,
    val zho: Zho? = null
)
package com.codetutor.countryinfoapp.util
import android.util.Log

class Logger(private val className: String) {

    fun info(message: String) {
        Log.i(className, message)
    }

    fun debug(message: String) {
        Log.d(className, message)
    }

    fun error(message: String, throwable: Throwable? = null) {
        Log.e(className, message, throwable)
    }
}
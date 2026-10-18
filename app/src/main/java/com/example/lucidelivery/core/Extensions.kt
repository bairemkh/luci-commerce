package com.example.lucidelivery.core

import retrofit2.HttpException
import java.io.IOException

fun Throwable.toUserMessage(): String = when (this) {
    is IOException -> "No connection. Showing saved data."
    is HttpException -> when (code()) {
        401 -> "Session expired. Please log in again."
        403 -> "You don't have permission."
        404 -> "Not found."
        in 500..599 -> "Server error. Try again later."
        else -> "Unexpected error (${code()})."
    }
    else -> message ?: "Unknown error"
}

fun Long.toPriceString(currency: String = "TND"): String =
    "%d.%03d $currency".format(this / 1000, this % 1000)   // TND uses 3 decimals (millimes)
// For 2-decimal currencies use: "%d.%02d".format(this / 100, this % 100)
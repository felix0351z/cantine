package de.juliando.app.utils

import java.text.DecimalFormat

//LATER: Check for currency format on device

private val formatter = DecimalFormat("0.#")

actual fun toCurrencyString(amount: Float): String {
    return "${formatter.format(amount)} €"
}
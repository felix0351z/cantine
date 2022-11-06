package de.felix0351.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun randomString(
    length: Int,
    complexCharset: Boolean = false
) : String {
    val charset = (('a'..'f') + ('0'..'9')).toMutableList()
    if (complexCharset) {
        charset += ('A'..'Z') + ('!' .. '/')
    }

    return (1..length).map {
        charset.random()
    }.joinToString("")
}

fun Any.getLogger(): Logger = LoggerFactory.getLogger(javaClass)
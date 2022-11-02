package de.felix0351.utils

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
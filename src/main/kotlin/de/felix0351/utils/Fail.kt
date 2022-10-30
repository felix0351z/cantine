package de.felix0351.utils

import kotlin.Exception
import kotlin.system.exitProcess

sealed class SQLException {
    class WrongSQLTypeException(type: String) : IllegalArgumentException("SQL Driver with the name $type can't be found!")
    class MissingArgumentsException() : IllegalArgumentException("There are missing arguments for the sql connection!")
}

fun fail(e: Exception): Nothing {
    e.printStackTrace()
    exitProcess(1)
}
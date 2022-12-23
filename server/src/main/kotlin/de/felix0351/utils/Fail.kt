package de.felix0351.utils

import kotlin.Exception
import kotlin.system.exitProcess

fun fail(e: Exception): Nothing {
    e.printStackTrace()
    exitProcess(1)
}
package de.juliando.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
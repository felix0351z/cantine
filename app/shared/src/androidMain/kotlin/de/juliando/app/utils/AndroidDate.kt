package de.juliando.app.utils

import kotlinx.datetime.*
import java.time.format.DateTimeFormatter

actual fun LocalDateTime.format(format: String): String =
    DateTimeFormatter.ofPattern(format).format(this.toJavaLocalDateTime())

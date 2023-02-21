package de.juliando.app.data

enum class StorageKeys {
    MEAL,
    REPORT,
    CATEGORY,
    SELECTION,
    ORDER,
    PAYMENT;

    val key get() = this.name
}
package de.juliando.app.data

/**
 *Keys for LocalDataStore
 */
enum class StorageKeys {
    MEAL,
    REPORT,
    ORDER,
    PAYMENT,
    COOKIE;

    val key get() = this.name
}
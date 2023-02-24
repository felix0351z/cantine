package de.juliando.app.data

/**
 *Keys for LocalDataStore
 */
enum class StorageKeys {
    MEAL,
    REPORT,
    CATEGORY,
    SELECTION,
    ORDER,
    PAYMENT,
    COOKIE;

    val key get() = this.name
}
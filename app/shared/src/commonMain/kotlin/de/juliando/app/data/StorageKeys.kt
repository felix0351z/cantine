package de.juliando.app.data

/**
 * Keys for the LocalDataStore.
 */
enum class StorageKeys {
    MEAL,
    REPORT,
    ORDER,
    PAYMENT,
    COOKIE,
    URL,
    USER;

    val key get() = this.name
}
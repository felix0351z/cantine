package de.felix0351.db

import io.ktor.server.sessions.*

class AuthenticationRepository : SessionStorage {




    override suspend fun invalidate(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun read(id: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun write(id: String, value: String) {
        TODO("Not yet implemented")
    }


}
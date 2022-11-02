package de.felix0351.utils

import org.mindrot.jbcrypt.BCrypt

object Hashing {

    private val pepper = FileHandler.configuration.authentication.pepper

    fun checkPassword(pw: String, hash: String) : Boolean {
        return BCrypt.checkpw(pw, hash)
    }

    fun toHash(password: String): String {
        return BCrypt.hashpw(pepper + password, BCrypt.gensalt())
    }


}

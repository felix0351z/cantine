package de.felix0351.utils

import org.mindrot.jbcrypt.BCrypt

//TODO
object Hashing {

    private val pepper = FileHandler.configuration.authentication.pepper

    fun checkPassword(pw: String, hash: String) : Boolean {
        return BCrypt.checkpw((pepper + pw), hash)
    }

    fun toHash(password: String): String {
        return BCrypt.hashpw(pepper + password, BCrypt.gensalt())
    }

    fun getCredit(creditHash: String): Float {
        return 20F

    }


}

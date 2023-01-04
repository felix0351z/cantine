package de.felix0351.utils

import org.mindrot.jbcrypt.BCrypt
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

//TODO
object Hashing {
    private val pepper = FileHandler.configuration.authentication.pepper
    private val algorithm = "AES/CBC/PKCS5Padding"
    private val key = SecretKeySpec(pepper.toByteArray(), "AES")
    private val iv = IvParameterSpec(ByteArray(16))

    fun checkPassword(pw: String, hash: String) : Boolean {
        return BCrypt.checkpw((pepper + pw), hash)
    }

    fun toHash(password: String): String {
        return BCrypt.hashpw(pepper + password, BCrypt.gensalt())
    }

    fun getCredit(encryptedCredit: String): Float {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, key, iv)
        val credit = cipher.doFinal(Base64.getDecoder().decode(encryptedCredit))
        return String(credit).toFloat
    }

    fun encryptCredit(credit: Float): String{
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.Encrypt_MODE, key, iv)
        val encryptedCredit = cipher.doFinal(credit.toString().toByteArray())
        return Base64.getEncoder().encodeToString(encryptedCredit)
    }
}

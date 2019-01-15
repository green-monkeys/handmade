package com.greenmonkeys.handmade.persistence

import at.favre.lib.crypto.bcrypt.BCrypt
import java.nio.charset.Charset
import java.util.regex.Pattern
import kotlin.random.Random

object Security {
    data class Password(val hash: String, val salt: String)

    fun isValidEmail(email: String): Boolean {
        val p =
            Pattern.compile("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
        val m = p.matcher(email)
        return m.matches()
    }

    // Generates salted and peppered bcrypt password hash
    fun generatePassword(password: String): Password {
        val salt = generateRandomString(10)
        val hash = BCrypt.withDefaults().hashToString(12, (password + salt + Random.nextInt(32,126).toChar()).toCharArray())
        return Password(hash, salt)
    }

    fun passwordIsValid(password: Password, guess: String): Boolean {
        for (i in 32..126) {
            val hash = BCrypt.withDefaults().hashToString(12, (guess + password.salt + i.toChar()).toCharArray())
            if (hash == password.hash)
                return true
        }
        return false
    }

    private fun generateRandomString(length: Int): String {
        val r = Random.nextBytes(length)
        return String(r, Charset.forName("UTF-8"))
    }
}
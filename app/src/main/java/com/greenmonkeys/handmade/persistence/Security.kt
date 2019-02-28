package com.greenmonkeys.handmade.persistence

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import at.favre.lib.crypto.bcrypt.BCrypt
import java.nio.charset.Charset
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.regex.Pattern
import kotlin.random.Random

object Security {
    data class Password(val hash: String, val salt: String)

    fun isValidEmail(email: String): Boolean {
        val p = Pattern.compile("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
        val m = p.matcher(email)
        return m.matches()
    }

    // Generates salted bcrypt password hash
    fun generatePasswordHash(password: String): Password {
        val salt = generateRandomString(10)
        val hash = BCrypt.withDefaults().hashToString(12, (password + salt).toCharArray())
        return Password(hash, salt)
    }

    private fun generateKeyPair(alias: String): KeyPair {
        val kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore")
        val parameterSpec = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_SIGN)
            .run{
                setDigests(KeyProperties.DIGEST_SHA512)
                build()
            }
        kpg.initialize(parameterSpec)
        return kpg.generateKeyPair()
    }

    fun passwordIsCorrect(password: Password, guess: String): Boolean {
        val verifyer = BCrypt.verifyer()
        val passwordIsValid = verifyer.verify((guess + password.salt).toCharArray(), password.hash)
        if (passwordIsValid.verified)
            return true
        return false
    }

    fun generateTemporaryPassword(): String {
        val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val lower = upper.toLowerCase()
        val numbers = "0123456789"
        val alphaNum = upper + lower + numbers
        var randomString = ""
        for (i in 0..10) {
            randomString += alphaNum[Random.nextInt(alphaNum.length)]
        }
        return randomString
    }

    private fun generateRandomString(length: Int): String {
        val r = Random.nextBytes(length)
        return String(r, Charset.forName("UTF-8"))
    }
}
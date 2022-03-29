package com.meghdut.nimie.ui.util

import android.content.ContentValues.TAG
import android.util.Log
import java.nio.charset.Charset
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object CryptoUtils {
    private const val RSA_ALGORITHM = "RSA"
    private val iv = byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private val ivspec = IvParameterSpec(iv)

    fun generateRSAKeyPair(): Pair<ByteArray, ByteArray> {

        val generator: KeyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM)

        generator.initialize(2048)
        val pair: KeyPair = generator.generateKeyPair()
        val privateKey: PrivateKey = pair.private
        val publicKey: PublicKey = pair.public

        return publicKey.encoded to privateKey.encoded
    }

    fun generateAESKey(): ByteArray {
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(128)
        val key = keygen.generateKey()

        return key.encoded
    }

    fun encryptRSA(unencryptedData: ByteArray, publicKeyBytes: ByteArray): ByteArray {
        val publicKey = loadPublicKey(publicKeyBytes)
        val cipher = Cipher.getInstance(RSA_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        return cipher.doFinal(unencryptedData)
    }

    fun decryptRSA(encryptedData: ByteArray, privateKeyBytes: ByteArray): ByteArray {
        val privateKey = loadPrivateKey(privateKeyBytes)
        val cipher: Cipher = Cipher.getInstance(RSA_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        return cipher.doFinal(encryptedData)
    }

    fun encryptAES(unencryptedData: ByteArray, keyBytes: ByteArray): ByteArray {
        val decodedKey = SecretKeySpec(keyBytes, "AES")
        val cipher2 = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher2.init(Cipher.ENCRYPT_MODE, decodedKey, ivspec)
        return cipher2.doFinal(unencryptedData)
    }

    fun decryptAES(encryptedData: ByteArray, keyBytes: ByteArray): ByteArray {
        val decodedKey = SecretKeySpec(keyBytes, "AES")
        val cipher2 = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher2.init(Cipher.DECRYPT_MODE, decodedKey, ivspec)
        return cipher2.doFinal(encryptedData)
    }

    private fun loadPublicKey(data: ByteArray): PublicKey {
        val spec = X509EncodedKeySpec(data)
        val fact: KeyFactory = KeyFactory.getInstance(RSA_ALGORITHM)
        return fact.generatePublic(spec)
    }

    private fun loadPrivateKey(data: ByteArray): PrivateKey {
        val keySpec = PKCS8EncodedKeySpec(data)
        val fact = KeyFactory.getInstance(RSA_ALGORITHM)
        val priv = fact.generatePrivate(keySpec)
        Arrays.fill(data, 0.toByte())
        return priv
    }


    fun test() {

        val plaintext: ByteArray = "Hello 1.2323!!".toByteArray(Charset.defaultCharset())

        val aesKey = generateAESKey()

        val encryptedData = encryptAES(plaintext,aesKey)

        val decryptedData = decryptAES(encryptedData,aesKey)

        Log.d(TAG, "test: ${String(decryptedData)}")



    }


}
package com.meghdut.nimie.ui.util

import android.util.Base64
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

object CryptoUtils {
    private const val ALGORITHM = "RSA"


    fun generateKeyPair(): Pair<String, String> {
        val generator: KeyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM)


        generator.initialize(2048)
        val pair: KeyPair = generator.generateKeyPair()
        val privateKey: PrivateKey = pair.private
//        println("The privatekey:  " + privateKey.encoded.toBase64())
        val publicKey: PublicKey = pair.public
//        println("The public key : " + publicKey.encoded.toBase64())
        return publicKey.encoded.toBase64() to privateKey.encoded.toBase64()
    }


    fun decrypt(encryptedData: String, encodedPrivateKey: String): String {
        return decrypt(encryptedData, loadPrivateKey(encodedPrivateKey))
    }

    fun encrypt(unencryptedData: String, encodedPublicKey: String): String {
        return encrypt(unencryptedData, loadPublicKey(encodedPublicKey))
    }


    private fun encrypt(unencryptedData: String, publicKey: PublicKey): String {
        val data = unencryptedData.toByteArray()
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        val encryptedBytes = cipher.doFinal(data)

        return encryptedBytes.toBase64()
    }

    private fun decrypt(encryptedData: String, privateKey: PrivateKey): String {
        val data = encryptedData.decodeBase64()
        val cipher: Cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        val decryptedBytes = cipher.doFinal(data)

        return String(decryptedBytes)
    }

    private fun ByteArray.toBase64(): String =
        Base64.encodeToString(this, Base64.DEFAULT)

    private fun String.decodeBase64() =

        Base64.decode(toByteArray(), Base64.DEFAULT)

    private fun loadPublicKey(encodedKey: String): PublicKey {
        val data: ByteArray = encodedKey.decodeBase64()
        val spec = X509EncodedKeySpec(data)
        val fact: KeyFactory = KeyFactory.getInstance(ALGORITHM)
        return fact.generatePublic(spec)
    }

    private fun loadPrivateKey(encodedKey: String): PrivateKey {
        val clear: ByteArray = encodedKey.decodeBase64()
        val keySpec = PKCS8EncodedKeySpec(clear)
        val fact = KeyFactory.getInstance(ALGORITHM)
        val priv = fact.generatePrivate(keySpec)
        Arrays.fill(clear, 0.toByte())
        return priv
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val (publicKey, privateKey) = generateKeyPair()

        val message = "Hello HUman !! hatt mc !!"

        val encryptedData = encrypt(message, publicKey)

        println("The encrypted data is $encryptedData ")

        val decryptedData = decrypt(encryptedData,privateKey)

        println("The decrypted Data $decryptedData")

    }

}
package com.meghdut.nimie.ui.util

import android.security.keystore.KeyProperties
import org.spongycastle.jce.provider.BouncyCastleProvider
import java.security.*
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


object CryptoUtils {
    private const val ALGORITHM = "RSA"


    fun generateKeyPair(): Pair<ByteArray, ByteArray> {

        val generator: KeyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM)


        generator.initialize(2048)
        val pair: KeyPair = generator.generateKeyPair()
        val privateKey: PrivateKey = pair.private
        val publicKey: PublicKey = pair.public

        return publicKey.encoded to privateKey.encoded
    }


    fun encrypt(unencryptedData: ByteArray, publicKeyBytes: ByteArray): ByteArray {
        val publicKey = loadPublicKey(publicKeyBytes)
        val cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)

        return cipher.doFinal(unencryptedData)
    }

    fun decrypt(encryptedData: ByteArray, privateKeyBytes: ByteArray): ByteArray {
        val privateKey = loadPrivateKey(privateKeyBytes)
        val cipher: Cipher = Cipher.getInstance(ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)

        return cipher.doFinal(encryptedData)
    }

    private fun loadPublicKey(data: ByteArray): PublicKey {
        val spec = X509EncodedKeySpec(data)
        val fact: KeyFactory = KeyFactory.getInstance(ALGORITHM)
        return fact.generatePublic(spec)
    }

    private fun loadPrivateKey(data: ByteArray): PrivateKey {
        val keySpec = PKCS8EncodedKeySpec(data)
        val fact = KeyFactory.getInstance(ALGORITHM)
        val priv = fact.generatePrivate(keySpec)
        Arrays.fill(data, 0.toByte())
        return priv
    }


}
package com.meghdut.nimie.repository

import android.util.Base64
import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.LocalUser
import com.meghdut.nimie.network.GrpcClient
import com.meghdut.nimie.ui.util.avatar
import com.meghdut.nimie.ui.util.randomName
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey

class UserRepository(db: NimieDb) {


    private val userDao = db.userDao()


    fun newUser(): LocalUser {
        val (publicKey, privateKey) = generateKeyPair()

        val name = randomName
        val avatar = avatar(name)

        val registerUserId = GrpcClient.createUser(publicKey)
        val userLocal = LocalUser(registerUserId, publicKey, privateKey, name, avatar, 1)
        userDao.clearActiveStatus()
        userDao.insert(userLocal)

        return userLocal
    }

    fun getCurrentActiveUser(): LocalUser {
        return userDao.getActiveUser()
    }

    private fun generateKeyPair(): Pair<String, String> {
        val generator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        val pair: KeyPair = generator.generateKeyPair()
        val privateKey: PrivateKey = pair.private
//        println("The privatekey:  " + privateKey.encoded.toBase64())
        val publicKey: PublicKey = pair.public
//        println("The public key : " + publicKey.encoded.toBase64())
        return publicKey.encoded.toBase64() to privateKey.encoded.toBase64()
    }

    private fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)


    fun anyActiveUser()  = userDao.anyActive() > 0

    fun logOutUser(){
        userDao.clearActiveStatus()
    }


}
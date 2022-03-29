package com.meghdut.nimie.repository

import com.meghdut.nimie.data.dao.NimieDb
import com.meghdut.nimie.data.model.LocalUser
import com.meghdut.nimie.network.GrpcClient
import com.meghdut.nimie.ui.util.CryptoUtils.generateRSAKeyPair
import com.meghdut.nimie.ui.util.randomName

class UserRepository(db: NimieDb) {


    private val userDao = db.userDao()


    fun newUser(): LocalUser {
        val (publicKey, privateKey) = generateRSAKeyPair()

        val name = randomName

        val registerUserId = GrpcClient.createUser(publicKey)
        val userLocal = LocalUser(registerUserId, publicKey, privateKey, name,  1)
        userDao.clearActiveStatus()
        userDao.insert(userLocal)

        return userLocal
    }

    fun getCurrentActiveUser(): LocalUser {
        return userDao.getActiveUser()
    }

    fun anyActiveUser()  = userDao.anyActive() > 0

    fun logOutUser(){
        userDao.clearActiveStatus()
    }


}
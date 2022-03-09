package com.meghdut.nimie.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.meghdut.nimie.dao.NimieDb
import com.meghdut.nimie.model.LocalUser

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db by lazy { NimieDb.create(application) }

    private val conversationDao by lazy { db.conversationDao() }

    private val userDao by lazy { db.userDao() }


    fun getConversationLiveData() = conversationDao.getConversationLive()

    fun getUser(): LocalUser = userDao.getUser()


}
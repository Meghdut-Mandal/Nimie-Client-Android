package com.meghdut.nimie.repository

import com.meghdut.nimie.dao.NimieDb
import com.meghdut.nimie.model.LocalStatus
import com.meghdut.nimie.network.GrpcClient

class StatusRepository(db: NimieDb)  {
    val statusDao = db.statusDao()

    fun createStatus(status: String,userId:Long): LocalStatus {

        val createdStatus = GrpcClient.createStatus(status, userId)

        statusDao.insert(createdStatus)

        return createdStatus
    }

}
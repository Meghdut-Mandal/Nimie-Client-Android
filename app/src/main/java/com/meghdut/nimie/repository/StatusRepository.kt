package com.meghdut.nimie.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.meghdut.nimie.dao.NimieDb
import com.meghdut.nimie.model.LocalStatus
import com.meghdut.nimie.network.GrpcClient

class StatusRepository(db: NimieDb) {
    private val statusDao = db.statusDao()

    private val statusLiveData = MutableLiveData<List<LocalStatus>>()

    // As of now only display online statuses

    fun createStatus(status: String, userId: Long): LocalStatus {
        val createdStatus = GrpcClient.createStatus(status, userId)
        statusDao.insert(createdStatus)

        return createdStatus
    }

    fun getStatus(): LiveData<List<LocalStatus>> {
        return statusLiveData
    }

    suspend fun loadStatus() {
        val bulkStatus = GrpcClient.getBulkStatus(0, 100)
        bulkStatus.forEach {
            println(it)
        }
        statusLiveData.postValue(bulkStatus)
    }


}
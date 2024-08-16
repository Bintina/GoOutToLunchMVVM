package com.bintina.goouttolunchmvvm.model.fcm

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class NotificationWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    val TAG = "NotificationWorkerLog"
    override fun doWork(): Result {
        Log.d(TAG, "Performing notification work doWork()")
        //TODO("Work")
        return Result.success()
    }
}
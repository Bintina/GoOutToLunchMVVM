package com.bintina.goouttolunchmvvm.restaurants.work

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource

class CustomWorkerFactory(private val dataSource: DataSource) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            DownloadWork::class.java.name ->
                DownloadWork(appContext, workerParameters, dataSource)
            else -> null // Return null to delegate to the default WorkerFactory
        }
    }
}
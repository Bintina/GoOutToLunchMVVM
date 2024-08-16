package com.bintina.goouttolunchmvvm.restaurants.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.database.places.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.deleteAllUsersWithRestaurantsFromRealtime
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.updateUserRestaurantChoiceToRoomObjects
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ResetWork(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

        val TAG = "ResetWorkLog"
    val CHANNEL_ID = "RESET_CHANNEL"
    val RESET_ID = 1002

    @JvmField
    val RESET_TITLE = "Reseting Local User Attendance Entries"

    @JvmField
    val RESET_CHANNEL_NAME = "WorkManager Reset"
    val RESET_CHANNEL_DESCRIPTION = "Reset attendance at midnight"

    override suspend fun doWork(): Result {
        //createResetChannel()

        val result = resetUsersAttendance()

        Log.d(TAG, "Coworker restaurant attended reset")

        return result
    }
  /*  private fun createResetChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, RESET_CHANNEL_NAME, importance).apply {
                description = RESET_CHANNEL_DESCRIPTION
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }*/

    private fun resetUsersAttendance(): ListenableWorker.Result {
        // Use a CompletableDeferred to handle asynchronous result in a synchronous context
        val deferred = CompletableDeferred<Result>()

        deleteAllUsersWithRestaurantsFromRealtime { result ->
            deferred.complete(result)
        }

        // Await the result and return it
        return runBlocking { deferred.await() }
    }

}

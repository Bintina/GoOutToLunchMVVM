package com.bintina.goouttolunchmvvm.restaurants.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.model.database.places.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.convertPlacesRestaurantListToLocalRestaurantList
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.saveRestaurantListToRoomDatabaseExtension
import com.bintina.goouttolunchmvvm.user.viewmodel.getWorkManagerUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DownloadWork(
    appContext: Context, workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    val TAG = "DownloadWorkLog"
    val CHANNEL_ID = "DOWNLOAD_CHANNEL"
    val DOWNLOAD_ID = 1001

    @JvmField
    val DOWNLOAD_TITLE = "Fetching Places to eat at"

    @JvmField
    val DOWNLOAD_CHANNEL_NAME = "WorkManager Download"
    val DOWNLOAD_CHANNEL_DESCRIPTION = "Downloads Restaurants whenever work starts"
    val DELAY_TIME_MILLIS: Long = 3000

    /**
     * Executes the background work for the download.
     */
    override suspend fun doWork(): Result {
        createDownloadChannel()

        val result = getPlacesRestaurantList()

        //set localUser.attending value to ""

        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_check_circle_24)
            setContentTitle(DOWNLOAD_TITLE)
            setContentText("Download completed successfully.")
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)
        }

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(TAG, "Notification permission not granted.")
            return Result.failure()
        }
        notificationManager.notify(DOWNLOAD_ID, notificationBuilder.build())

        Log.d(TAG, "Worker reached success() return")
        return result
    }


    /**
     * Creates a notification channel for Android Oreo (API level 26) and above.
     */
    private fun createDownloadChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, DOWNLOAD_CHANNEL_NAME, importance).apply {
                description = DOWNLOAD_CHANNEL_DESCRIPTION
            }

            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    //.......................Call.............................................
    private suspend fun getPlacesRestaurantList(): Result {

        val dataSource =
            DataSource

        return withContext(Dispatchers.IO) {
            try {
                val result = dataSource.loadRestaurantList(this)
                if (result.isEmpty()) {
                    Log.d(TAG, "result list is empty")
                    Result.failure()
                } else {
                    Log.d(TAG, "result list has ${result.size} items.")
                    saveListToRoomDatabase(result)
                    Result.success()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching restaurant list", e)
                Result.failure()
            }

        }
    }

//.......................Response.............................................


    suspend fun saveListToRoomDatabase(result: List<Restaurant>) {
        if (result.isNullOrEmpty()) {
            Log.d(TAG, "result is null or empty. Local list is not updated")
        } else {
            val user = getWorkManagerUser()
            val localRestaurantList =
                convertPlacesRestaurantListToLocalRestaurantList(result, user)
            //Log.d(TAG, "localRestaurantList is $localRestaurantList")
            if (localRestaurantList != null && localRestaurantList.isNotEmpty()) {
                saveRestaurantListToRoomDatabaseExtension(localRestaurantList)
            }
        }
//Clean up User attending constructor
    }
}
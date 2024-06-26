package com.bintina.goouttolunchmvvm.restaurants.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewModelScope
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.RestaurantViewModel
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.convertPlacesRestaurantListToLocalRestaurantList
import com.bintina.goouttolunchmvvm.restaurants.viewmodel.saveListToRoomDatabase
import com.bintina.goouttolunchmvvm.utils.MainActivity
import com.bintina.goouttolunchmvvm.utils.MyApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DownloadWork(
    appContext: Context, workerParams: WorkerParameters,
    private val dataSource: DataSource
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
// Create Download channel
        createDownloadChannel()

        // Set notification channel and ID
        val downloadChannelId = CHANNEL_ID
        val downloadId = DOWNLOAD_ID

        // Call getPlacesRestaurantList
        val result = getPlacesRestaurantList()

        // Handle notification Click
        val mainIntent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val mainPendingIntent = PendingIntent.getActivity(
            applicationContext, 1, mainIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Set up second button intent
        val secondBtnIntent = Intent(applicationContext, MainActivity::class.java)
        val secondBtPendingIntent = PendingIntent.getActivity(
            applicationContext, 2, secondBtnIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // Build the notification
        val downloadBuilder =
            NotificationCompat.Builder(applicationContext, downloadChannelId).apply {
                setSmallIcon(R.drawable.ic_baseline_check_circle_24)
                setContentTitle(DOWNLOAD_TITLE)
                priority = NotificationCompat.PRIORITY_DEFAULT
                setAutoCancel(true)
                setContentIntent(mainPendingIntent)
                addAction(
                    com.google.android.material.R.drawable.mtrl_ic_cancel,
                    "Dismiss",
                    secondBtPendingIntent
                )
            }

        // Notify using NotificationManagerCompat
        val downloadManagerCompat = NotificationManagerCompat.from(applicationContext)
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.failure()
        }
        downloadManagerCompat.notify(downloadId, downloadBuilder.build())

        // Indicate whether the work finished successfully with the Result
        Log.d(TAG, "Worker reached success() return")
        return result
    }


    /**
     * Creates a notification channel for Android Oreo (API level 26) and above.
     */
    private fun createDownloadChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = DOWNLOAD_CHANNEL_NAME
            val description = DOWNLOAD_CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val notificationChannel = NotificationChannel(CHANNEL_ID, name, importance)
            notificationChannel.description = description

            val notificationManager =
                applicationContext.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)


        }
    }

    //.......................Call.............................................
    private suspend fun getPlacesRestaurantList(): Result {


        return withContext(Dispatchers.IO) {
            val result = try {
                dataSource.loadRestaurantList(this)
            } catch (e: Exception) {
                Log.d(TAG, "Error is $e. Cause is ${e.cause}")
                emptyList<Restaurant?>()
            }

            if (result.isEmpty()) {
                Log.d(TAG, "result list is empty")
                return@withContext Result.failure()
            } else {
                Log.d(TAG, "result list has ${result.size} items.")
                saveListToRoomDatabase(result)
                return@withContext Result.success()
            }
        }
    }

//.......................Response.............................................


    suspend fun saveListToRoomDatabase(result: List<Restaurant?>) {

        val placesRestaurantList = result.toMutableList()
        val localRestaurantList =
            convertPlacesRestaurantListToLocalRestaurantList(placesRestaurantList)
        Log.d(TAG, "localRestaurantList is $localRestaurantList")

        val db = MyApp.db

        withContext(Dispatchers.IO) {
            localRestaurantList.forEach { localRestaurant ->
                Log.d(TAG, "Inserting: $localRestaurant")
                db.restaurantDao().insertRestaurant(localRestaurant!!)
            }
        }
    }

}
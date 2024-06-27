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

        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_baseline_check_circle_24)
            setContentTitle(DOWNLOAD_TITLE)
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(true)
        }

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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

            val notificationManager = applicationContext.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

        }
    }

    //.......................Call.............................................
    private suspend fun getPlacesRestaurantList(): Result {

            val dataSource =
                com.bintina.goouttolunchmvvm.restaurants.model.database.repository.DataSource

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


    suspend fun saveListToRoomDatabase(result: List<Restaurant?>) {

        val localRestaurantList =
            convertPlacesRestaurantListToLocalRestaurantList(result)
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
package com.bintina.goouttolunchmvvm.model.database.repositories

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.UserRestaurantCrossRef
import com.bintina.goouttolunchmvvm.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.model.database.dao.UserDao

@Database(entities = [LocalUser::class, LocalRestaurant::class, UserRestaurantCrossRef::class], version = 13)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigrationFrom(12)
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }
}
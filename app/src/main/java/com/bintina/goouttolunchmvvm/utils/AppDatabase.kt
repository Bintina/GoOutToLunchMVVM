package com.bintina.goouttolunchmvvm.utils

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao

@Database(entities = [LocalUser::class,LocalRestaurant::class], version = 8, autoMigrations = [
    AutoMigration (from = 7, to = 8, spec = MyAutoMigrationSpec::class)
])
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )

                    .build()
                instance = newInstance
                newInstance
            }
        }
      /*  private val MIGRATION_7_8 = object : Migration(7, 8) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Rename the old column
                db.execSQL(
                    "ALTER TABLE LocalRestaurant RENAME COLUMN attending TO attendingList"
                )

                // Add new columns
                db.execSQL(
                    "ALTER TABLE LocalRestaurant ADD COLUMN currentUserName TEXT DEFAULT '' NOT NULL"
                )
                db.execSQL(
                    "ALTER TABLE LocalRestaurant ADD COLUMN currentUserUid TEXT DEFAULT '' NOT NULL"
                )
                db.execSQL(
                    "ALTER TABLE LocalRestaurant ADD COLUMN currentUserAttendingBoolean INTEGER DEFAULT 0 NOT NULL"
                )
            }
        }*/
    }
}
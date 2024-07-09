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

@Database(entities = [LocalUser::class,LocalRestaurant::class], version = 7, autoMigrations = [
    AutoMigration (from = 6, to = 7)
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
                    .addMigrations(MIGRATION_5_6)
                    .fallbackToDestructiveMigration() // This will clear data on schema change
                    .build()
                instance = newInstance
                newInstance
            }
        }
        private val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create the new table
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS LocalRestaurant_new (" +
                            "restaurantId TEXT NOT NULL, " +
                            "name TEXT NOT NULL, " +
                            "address TEXT NOT NULL, " +
                            "latitude REAL NOT NULL, " +
                            "longitude REAL NOT NULL, " +
                            "photoUrl TEXT, " +
                            "attending TEXT NOT NULL, " + // Change to TEXT
                            "createdAt INTEGER NOT NULL, " +
                            "updatedAt INTEGER NOT NULL, " +
                            "visited INTEGER NOT NULL, " + // Add visited field
                            "PRIMARY KEY(restaurantId)" +
                            ")"
                )

                // Copy data from the old table to the new table
                db.execSQL(
                    "INSERT INTO LocalRestaurant_new (restaurantId, name, address, latitude, longitude, photoUrl, attending, createdAt, updatedAt, visited) " +
                            "SELECT restaurantId, name, address, latitude, longitude, photoUrl, CAST(attending AS TEXT), createdAt, updatedAt, 0 " + // Default value for visited
                            "FROM LocalRestaurant"
                )

                // Remove the old table
                db.execSQL("DROP TABLE LocalRestaurant")

                // Rename the new table to the old table name
                db.execSQL("ALTER TABLE LocalRestaurant_new RENAME TO LocalRestaurant")
            }
        }
    }
}
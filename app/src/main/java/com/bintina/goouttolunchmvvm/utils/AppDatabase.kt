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

@Database(entities = [LocalUser::class,LocalRestaurant::class], version = 9, autoMigrations = [
    AutoMigration (from = 8, to = 9, spec = MyAutoMigrationSpec::class)
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
                    .addMigrations(MIGRATION_8_9)
                    .build()
                instance = newInstance
                newInstance
            }
        }
        private val MIGRATION_8_9 = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Step 1: Add a new non-null column with a default value
                database.execSQL("ALTER TABLE LocalUser ADD COLUMN new_attending_string TEXT NOT NULL DEFAULT ''")

                // Step 2: Copy data from the old column to the new column (if necessary)
                database.execSQL("UPDATE LocalUser SET new_attending_string = attending_string WHERE attending_string IS NOT NULL")

                // Step 3: Drop the old column (not possible directly in SQLite, hence the renaming approach)
                // So we create a new table with the correct schema and copy the data
                database.execSQL("""
            CREATE TABLE new_LocalUser (
                uid TEXT NOT NULL PRIMARY KEY,
                display_name TEXT NOT NULL,
                email TEXT NOT NULL,
                profile_picture_url TEXT,
                attending_string TEXT NOT NULL DEFAULT '',
                created_at INTEGER NOT NULL,
                updated_at INTEGER NOT NULL
            )
        """.trimIndent())

                // Copy the data from the old table to the new table
                database.execSQL("""
            INSERT INTO new_LocalUser (uid, display_name, email, profile_picture_url, attending_string, created_at, updated_at)
            SELECT uid, display_name, email, profile_picture_url, new_attending_string, created_at, updated_at
            FROM LocalUser
        """.trimIndent())

                // Drop the old table
                database.execSQL("DROP TABLE LocalUser")

                // Rename the new table to the old table name
                database.execSQL("ALTER TABLE new_LocalUser RENAME TO LocalUser")
            }
        }
    }
}
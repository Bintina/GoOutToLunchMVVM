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

@Database(entities = [LocalUser::class,LocalRestaurant::class], version = 11, autoMigrations = [
    AutoMigration (from = 9, to = 11, spec = MyAutoMigrationSpec::class)
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
                    .addMigrations(MIGRATION_9_11)
                    .build()
                instance = newInstance
                newInstance
            }
        }
        private val MIGRATION_9_11 = object : Migration(9, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Remove duplicate display names from LocalUser
                database.execSQL(
                    """
                    DELETE FROM LocalUser
                    WHERE uid NOT IN (
                        SELECT MIN(uid) FROM LocalUser GROUP BY display_name
                    )
                """.trimIndent()
                )

                // Check for inconsistencies before migration
                database.execSQL(
                    """
                    DELETE FROM LocalRestaurant 
                    WHERE currentUserUid NOT IN (SELECT uid FROM LocalUser)
                """.trimIndent()
                )

                database.execSQL(
                    """
                    DELETE FROM LocalRestaurant 
                    WHERE currentUserName NOT IN (SELECT display_name FROM LocalUser)
                """.trimIndent()
                )

                // Create a new table with the foreign key relationship
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `LocalRestaurant_new` (
                        `restaurantId` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `address` TEXT NOT NULL,
                        `latitude` REAL NOT NULL,
                        `longitude` REAL NOT NULL,
                        `photoUrl` TEXT,
                        `attendingList` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL,
                        `updatedAt` INTEGER NOT NULL,
                        `visited` INTEGER NOT NULL,
                        `currentUserName` TEXT NOT NULL DEFAULT '',
                        `currentUserUid` TEXT NOT NULL DEFAULT '',
                        `currentUserAttendingBoolean` INTEGER NOT NULL DEFAULT 0,
                        PRIMARY KEY(`restaurantId`),
                        FOREIGN KEY(`currentUserUid`) REFERENCES `LocalUser`(`uid`) ON DELETE CASCADE,
                        FOREIGN KEY(`currentUserName`) REFERENCES `LocalUser`(`display_name`) ON DELETE CASCADE
                    )
                """.trimIndent()
                )

                // Copy the data from the old table to the new table
                database.execSQL(
                    """
                    INSERT INTO `LocalRestaurant_new` (
                        `restaurantId`, `name`, `address`, `latitude`, `longitude`, `photoUrl`, 
                        `attendingList`, `createdAt`, `updatedAt`, `visited`, 
                        `currentUserName`, `currentUserUid`, `currentUserAttendingBoolean`
                    )
                    SELECT 
                        `restaurantId`, `name`, `address`, `latitude`, `longitude`, `photoUrl`, 
                        `attendingList`, `createdAt`, `updatedAt`, `visited`, 
                        `currentUserName`, `currentUserUid`, `currentUserAttendingBoolean`
                    FROM `LocalRestaurant`
                """.trimIndent()
                )

                // Remove the old table
                database.execSQL("DROP TABLE `LocalRestaurant`")

                // Rename the new table to the original table name
                database.execSQL("ALTER TABLE `LocalRestaurant_new` RENAME TO `LocalRestaurant`")

                // Create the indices for the foreign key columns
                database.execSQL("CREATE INDEX `index_LocalRestaurant_currentUserUid` ON `LocalRestaurant` (`currentUserUid`)")
                database.execSQL("CREATE INDEX `index_LocalRestaurant_currentUserName` ON `LocalRestaurant` (`currentUserName`)")

                // Create the unique index for the displayName column in LocalUser
                database.execSQL("CREATE UNIQUE INDEX `index_LocalUser_displayName` ON `LocalUser` (`display_name`)")
            }
        }
    }
}
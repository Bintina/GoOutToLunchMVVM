package com.bintina.goouttolunchmvvm.model.database.repositories

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bintina.goouttolunchmvvm.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.model.LocalUser
import com.bintina.goouttolunchmvvm.model.UserRestaurantCrossRef
import com.bintina.goouttolunchmvvm.model.database.dao.UserDao

@Database(entities = [LocalUser::class, LocalRestaurant::class, UserRestaurantCrossRef::class], version = 12, autoMigrations = [
    AutoMigration (from =9, to = 12, spec = MyAutoMigrationSpec::class)
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
                    .addMigrations(MIGRATION_9_12)
                    .build()
                instance = newInstance
                newInstance
            }
        }
        private val MIGRATION_9_12 = object : Migration(9, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Step 1: Create the new tables
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `LocalUser_new` (
                `uid` TEXT NOT NULL,
                `display_name` TEXT NOT NULL,
                `email` TEXT NOT NULL,
                `profile_picture_url` TEXT,
                `created_at` INTEGER NOT NULL,
                `updated_at` INTEGER NOT NULL,
                PRIMARY KEY(`uid`)
            )
            """.trimIndent()
                )

                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `LocalRestaurant_new` (
                `restaurantId` TEXT NOT NULL,
                `name` TEXT NOT NULL,
                `address` TEXT NOT NULL,
                `latitude` REAL NOT NULL,
                `longitude` REAL NOT NULL,
                `photoUrl` TEXT,
                `createdAt` INTEGER NOT NULL,
                `updatedAt` INTEGER NOT NULL,
                `visited` INTEGER NOT NULL,
                PRIMARY KEY(`restaurantId`)
            )
            """.trimIndent()
                )

                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS `UserRestaurantCrossRef` (
                `uid` TEXT NOT NULL,
                `restaurantId` TEXT NOT NULL,
                PRIMARY KEY(`userId`, `restaurantId`),
                FOREIGN KEY(`userId`) REFERENCES `LocalUser`(`uid`) ON DELETE CASCADE,
                FOREIGN KEY(`restaurantId`) REFERENCES `LocalRestaurant`(`restaurantId`) ON DELETE CASCADE
            )
            """.trimIndent()
                )

                // Step 2: Copy the data from the old tables to the new tables
                database.execSQL(
                    """
            INSERT INTO `LocalUser_new` (
                `uid`, `display_name`, `email`, `profile_picture_url`, `created_at`, `updated_at`
            )
            SELECT 
                `uid`, `display_name`, `email`, `profile_picture_url`, `created_at`, `updated_at`
            FROM `LocalUser`
            """.trimIndent()
                )

                database.execSQL(
                    """
            INSERT INTO `LocalRestaurant_new` (
                `restaurantId`, `name`, `address`, `latitude`, `longitude`, `photoUrl`, 
                `createdAt`, `updatedAt`, `visited`
            )
            SELECT 
                `restaurantId`, `name`, `address`, `latitude`, `longitude`, `photoUrl`, 
                `createdAt`, `updatedAt`, `visited`
            FROM `LocalRestaurant`
            """.trimIndent()
                )

                // Assuming you have some mechanism to populate the UserRestaurantCrossRef table
                // This could be a more complex query based on your specific data
                database.execSQL(
                    """
            INSERT INTO `UserRestaurantCrossRef` (`uid`, `restaurantId`)
            SELECT DISTINCT `currentUserUid`, `restaurantId` 
            FROM `LocalRestaurant` 
            WHERE `currentUserUid` IS NOT NULL AND `currentUserUid` != ''
            """.trimIndent()
                )

                // Step 3: Drop the old tables
                database.execSQL("DROP TABLE `LocalUser`")
                database.execSQL("DROP TABLE `LocalRestaurant`")

                // Step 4: Rename the new tables to the original names
                database.execSQL("ALTER TABLE `LocalUser_new` RENAME TO `LocalUser`")
                database.execSQL("ALTER TABLE `LocalRestaurant_new` RENAME TO `LocalRestaurant`")

                // Step 5: Create indices for the new tables

                database.execSQL("CREATE INDEX `index_UserRestaurantCrossRef_userId` ON `UserRestaurantCrossRef` (`uid`)")
                database.execSQL("CREATE INDEX `index_UserRestaurantCrossRef_restaurantId` ON `UserRestaurantCrossRef` (`restaurantId`)")
            }
        }
    }
}
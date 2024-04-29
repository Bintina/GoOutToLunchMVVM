package com.bintina.goouttolunchmvvm.user.model.database


import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class SaveUserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {
        //Singleton
        @Volatile
        private var INSTANCE: SaveUserDatabase? = null

        //Instance
        fun getInstance(context: Context): SaveUserDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): SaveUserDatabase {
            return Room.databaseBuilder(context.applicationContext, SaveUserDatabase::class.java, "MyDatabase.db")
                .addCallback(prepopulateDatabase())
                .build()
        }


        //
        private fun prepopulateDatabase(): RoomDatabase.Callback {
            return object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val contentValues = ContentValues()
                    contentValues.put("id", 1)
                    contentValues.put("userName", "Philippe")
                    contentValues.put(
                        "profilePicture",
                        "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2"
                    )
                    db.insert("User", OnConflictStrategy.IGNORE, contentValues)
                }
            }
        }
    }
}
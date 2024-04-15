package com.bintina.goouttolunchmvvm.login.model.database


import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bintina.goouttolunchmvvm.login.model.User
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.RestaurantDao

class SaveUserRestaurantsDatabase {
    @Database(
        entities = arrayOf(
            User::class,
            Restaurant::class),
        version = 1,
        exportSchema = false)

    abstract class SaveUserRestaurantsDatabase: RoomDatabase(){
        //Dao
        abstract  fun userDao(): UserDao

        abstract fun restaurantDao(): RestaurantDao

        companion object {

            //Singleton
            @Volatile
            private var INSTANCE: SaveUserRestaurantsDatabase? = null

            //Instance
            fun getInstance(context: Context): SaveUserRestaurantsDatabase? {
                if (INSTANCE == null){
                    synchronized(SaveUserRestaurantsDatabase::class.java){
                        if (INSTANCE == null){
                            INSTANCE = Room.databaseBuilder(context.applicationContext,
                                SaveUserRestaurantsDatabase::class.java,
                                "MyDatabase.db")
                                .addCallback(prepopulateDatabase())
                                .build()
                        }
                    }
                }
                return INSTANCE
            }


            //
            private fun prepopulateDatabase(): RoomDatabase.Callback{
                return object : RoomDatabase.Callback(){

                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)

                        val contentValues = ContentValues()
                        contentValues.put("id", 1)
                        contentValues.put("name", "Philippe")
                        contentValues.put("profilePicture", "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2")
                        db.insert("User", OnConflictStrategy.IGNORE, contentValues)
                    }
                }
            }
        }
    }
}
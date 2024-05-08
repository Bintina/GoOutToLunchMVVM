package com.bintina.goouttolunchmvvm.restaurants.model.database.repository

import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.dao.RestaurantDao
import com.bintina.goouttolunchmvvm.user.model.database.SaveUserDatabase

@Database(entities = [Restaurant::class], version = 1, exportSchema = false)
abstract class SaveRestaurantDatabase: RoomDatabase() {

    abstract fun restaurantDao(): RestaurantDao

    companion object{
        //Singleton
        @Volatile
        private var INSTANCE: SaveRestaurantDatabase? = null

        fun getInstance(context: Context): SaveRestaurantDatabase {
            return INSTANCE?: synchronized(this){
                INSTANCE?: buildDatabase(context).also { INSTANCE = it}
            }
        }

        private fun buildDatabase(context: Context): SaveRestaurantDatabase {
            return Room.databaseBuilder(context.applicationContext, SaveRestaurantDatabase::class.java, "MyRestaurantDatabase.db")
                .addCallback(prepopulateDatabase())
                .build()
        }

        private fun prepopulateDatabase(): Callback {
            return object : Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    val contentValues = ContentValues()
                    contentValues.put("id", 10)
                    contentValues.put("name","YummyPalace")
                    contentValues.put("photoUrl", "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2")
                    contentValues.put("numberOfInterestedUsers","5")
                    contentValues.put("visited", true)
                    db.insert("Restaurant", OnConflictStrategy.IGNORE, contentValues)
                }
            }
        }
    }
}
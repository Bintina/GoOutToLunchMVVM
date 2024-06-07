package com.bintina.goouttolunchmvvm.utils

class TempClass {
    /* Strings
    <resources>
    <string name="app_name">Go Out To Lunch MVVM</string>
</resources>
     */

    /* themes.xml
    resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.GoOutToLunchMVVM" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your light theme here. -->
        <!-- <item name="colorPrimary">@color/my_light_primary</item> -->
    </style>

    <style name="Theme.GoOutToLunchMVVM" parent="Base.Theme.GoOutToLunchMVVM" />
</resources>
     */

    /*themes.xml(night)
    <resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.GoOutToLunchMVVM" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your dark theme here. -->
        <!-- <item name="colorPrimary">@color/my_dark_primary</item> -->
    </style>
</resources>
     */

/*    testOptions {
        unitTests {
            // Disable StrictMode for instrumented tests
            all {
                Instrumented.systemProperty(
                    "org.junit.runners.JUnitCore.defaultResourceName",
                    "JUnit3"
                )
            }
        }
    }*/
    /*     @RequiresApi(Build.VERSION_CODES.P)
    fun getSha() {
        val packageName = "com.bintina.goouttolunchmvvm"

        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)

            for (signature in info.signingInfo.signingCertificateHistory) {

                // Compute SHA-1 fingerprint
                val sha1 = MessageDigest.getInstance("SHA-1")
                sha1.update(signature.toByteArray())
                val sha1Hex = bytesToHex(sha1.digest())
                Log.d("SHA-1", sha1Hex)

                //SHA-256
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                md.update(signature.toByteArray())

                // Convert the byte array to hexadecimal representation
                val sha256Hex = bytesToHex(md.digest())

                Log.d("SHA", sha256Hex)

                val keyHash = String(Base64.encode(md.digest(), Base64.NO_WRAP))
                Log.e("hash key", keyHash)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("name not found", e.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (i in bytes.indices) {
            val v = bytes[i].toInt() and 0xFF
            hexChars[i * 2] = "0123456789ABCDEF"[v shr 4]
            hexChars[i * 2 + 1] = "0123456789ABCDEF"[v and 0x0F]
        }
        return String(hexChars)
    }*/
}

/* SaveUserRestaurantsDatabase code.
package com.bintina.goouttolunchmvvm.login.model.database


import android.content.ContentValues
import android.content.Context
import androidx.room.Database
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bintina.goouttolunchmvvm.login.model.User
import com.bintina.goouttolunchmvvm.login.model.database.dao.UserDao
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
}*/

/*    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //val argsUserName = safeArgs.userName
        var argsUserName = safeArgs.userName

        Log.d("MyLoginFragLog","safe args userName value is $argsUserName")
        // Create a new Bundle and set the new value
        val newArgs = Bundle().apply {
            putString("userName", "Belladona")
        }
        Log.d("MyLoginFragLog","newArgs value is $newArgs")
        val newArgValue = safeArgs.userName
        Log.d("MyLoginFragLog","new safe args userName value is $newArgValue")



        Log.d("LoginFragLog", "LoginFragment inflated")
    }*/


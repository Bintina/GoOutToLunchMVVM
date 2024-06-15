package com.bintina.goouttolunchmvvm.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bintina.goouttolunchmvvm.LiveDataTestUtil
import com.bintina.goouttolunchmvvm.user.model.User
import com.bintina.goouttolunchmvvm.user.model.database.SaveUserDatabase
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoInstrumentedTest {
    private var database: SaveUserDatabase? = null

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        this.database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            SaveUserDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        database?.close()
    }

    //Data Set for Test

    private val USER_ID: String = "1"
    private val USER_DEMO =
        User(USER_ID,
            "Philippe",
            "philippe@example.com",
            "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2")
            //"https://picsum.photos/seed/picsum/200/200")
   /* @Test
    @Throws(InterruptedException::class)
    fun insertUser() {
        //Before: Adding a new user
        this.database?.userDao()?.createUser(USER_DEMO)
        //Test
        val user = LiveDataTestUtil.getValue(this.database?.userDao()?.getUser(USER_ID))
        assertTrue(user.name == USER_DEMO.name && user.userId == USER_DEMO.userId)

    }*/

    /*
    {
  "kind": "identitytoolkit#GetAccountInfoResponse",
  "users": [
    {
      "localId": "aIQFBIfL2KhyZgqPFhUqhLUvaSO2",
      "email": "dellyemaya@gmail.com",
      "displayName": "BintinaTamar",
      "photoUrl": "https://lh3.googleusercontent.com/a/ACg8ocLRH_5BWPTVps3HeCyJzzu1mWrRWsiU8_DXlGanOjt0FQ02NDE=s96-c",
      "emailVerified": true,
      "providerUserInfo": [
        {
          "providerId": "google.com",
          "displayName": "BintinaTamar",
          "photoUrl": "https://lh3.googleusercontent.com/a/ACg8ocLRH_5BWPTVps3HeCyJzzu1mWrRWsiU8_DXlGanOjt0FQ02NDE=s96-c",
          "federatedId": "108790554882732525755",
          "email": "dellyemaya@gmail.com",
          "rawId": "108790554882732525755"
        }
      ],
      "validSince": "1717810698",
      "lastLoginAt": "1717810698404",
      "createdAt": "1717810698403",
      "lastRefreshAt": "2024-06-08T01:38:18.404Z"
    }
  ]
}
     */
}
package com.bintina.goouttolunchmvvm.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bintina.goouttolunchmvvm.model.LocalUser

import com.bintina.goouttolunchmvvm.model.database.repositories.AppDatabase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import java.time.LocalDateTime
import java.time.ZoneOffset

@RunWith(AndroidJUnit4::class)
class LocalUserDaoInstrumentedTest {
    private var database: AppDatabase? = null

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun initDb() {
        this.database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            AppDatabase::class.java
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

    // Dummy date and time: December 31, 2022, 23:59:59
    val dummyUpdateDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59)
    val updatedAt = dummyUpdateDate.toEpochSecond(ZoneOffset.UTC).toLong()

    // Dummy date and time: December 31, 2023, 23:59:59
    val dummyCreatedDate = LocalDateTime.of(2021, 12, 31, 23, 59, 59)
    val createdAt = dummyCreatedDate.toEpochSecond(ZoneOffset.UTC).toLong()
    private val USER_ID: String = "1"
    private val LocalUSER_DEMO =
        LocalUser(
            USER_ID,
            "Philippe",
            "philippe@example.com",
            "https://oc-user.imgix.net/users/avatars/15175844164713_frame_523.jpg?auto=compress,format&q=80&h=100&dpr=2",
            "Pinapple land",
            createdAt,
            updatedAt
        )
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
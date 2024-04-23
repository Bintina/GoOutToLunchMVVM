package com.bintina.goouttolunchmvvm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
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

    private val USER_ID: Long = 1
    private val USER_DEMO =
        User(USER_ID,
            "Philippe",
            1,
            "https://picsum.photos/seed/picsum/200/200")
    @Test
    @Throws(InterruptedException::class)
    fun insertUser() {
        //Before: Adding a new user
        this.database?.userDao()?.createUser(USER_DEMO)
        //Test
        val user = LiveDataTestUtil.getValue(this.database?.userDao()?.getUser(USER_ID))
        assertTrue(user.name == USER_DEMO.name && user.userId == USER_DEMO.userId)

    }
}
package com.bintina.goouttolunchmvvm.user.viewmodel.injection

import android.content.Context
import androidx.room.Room
import com.bintina.goouttolunchmvvm.user.model.database.dao.UserDao
import com.bintina.goouttolunchmvvm.user.viewmodel.UserViewModel
import com.bintina.goouttolunchmvvm.user.viewmodel.ViewModelFactory
import com.bintina.goouttolunchmvvm.utils.AppDatabase
import com.bintina.goouttolunchmvvm.utils.MyApp
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    private fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "database-name")
            .fallbackToDestructiveMigration()
            .build()
    }

    private fun provideUserDao(database: AppDatabase): UserDao {
        return database.userDao()
    }

    fun provideExecutor(): Executor{
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val userDao = provideUserDao(provideDatabase(context))
        val application = MyApp()
        return ViewModelFactory(application, userDao)
    }

    fun provideUserViewModel(context: Context): UserViewModel {
        val userDao = provideUserDao(provideDatabase(context))
        val application = MyApp() // Assuming MyApp extends Application
        val factory = ViewModelFactory(application,userDao)
        return factory.create(UserViewModel::class.java)
    }
}
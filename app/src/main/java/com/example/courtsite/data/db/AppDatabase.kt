package com.example.courtsite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.courtsite.data.model.User

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
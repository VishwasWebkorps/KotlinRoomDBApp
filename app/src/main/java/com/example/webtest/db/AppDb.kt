package com.example.webtest.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.webtest.dao.UserDao


@Database(entities = [(UserEntity::class)],version = 1)
abstract class AppDb : RoomDatabase() {

    abstract fun bookDao(): UserDao
}
package com.example.webtest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.webtest.db.UserEntity


@Dao
interface UserDao
{
    @Insert
    fun saveUserNameEmail(name: UserEntity,email :UserEntity)

    @Query(value = "Select * from UserEntity")
    fun getAllUser() : List<UserEntity>
}
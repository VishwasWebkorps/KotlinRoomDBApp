package com.example.webtest.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class UserEntity {

    @PrimaryKey
    var bookId: Int =0

    @ColumnInfo(name ="UserName")
    var userName:  String =""

    @ColumnInfo(name ="UserEmail")
    var userEmail:  String =""

}
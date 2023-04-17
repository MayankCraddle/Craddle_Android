package com.cradle.roomDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val phone_num: String
)



package com.example.courtsite.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String? = null,
    val email: String?,
    val phone: String?,
    val password: String,

    @ColumnInfo(name = "profile_picture")
<<<<<<< HEAD
    val profilePicture: String? = null,

    @ColumnInfo(name = "registration_date")
    val registrationDate: Long = System.currentTimeMillis()
=======
    val profilePicture: String? = null
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
)
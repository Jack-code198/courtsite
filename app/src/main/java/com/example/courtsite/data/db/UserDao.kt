package com.example.courtsite.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.courtsite.data.model.User

@Dao
interface UserDao {

    // Insert or update user
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    // Login: Find user by email or phone and match password
    @Query("""
        SELECT * FROM users 
        WHERE (email = :identifier OR phone = :identifier) 
        AND password = :password 
        LIMIT 1
    """)
    suspend fun findUser(identifier: String, password: String): User?

    // Find user by email or phone (for registration or profile fetch)
    @Query("""
        SELECT * FROM users 
        WHERE (email = :identifier OR phone = :identifier) 
        LIMIT 1
    """)
    suspend fun findUserByIdentifier(identifier: String): User?

    // Update profile picture for a specific user
    @Query("""
        UPDATE users 
        SET profile_picture = :profilePic 
        WHERE email = :identifier OR phone = :identifier
    """)
    suspend fun updateProfilePicture(identifier: String, profilePic: String)

    // Update contact info (email/phone) for a specific user
    @Query("""
        UPDATE users
        SET name = :newName,
            email = :newEmail,
            phone = :newPhone
        WHERE email = :identifier OR phone = :identifier
    """)
    suspend fun updateContact(identifier: String, newName: String?, newEmail: String?, newPhone: String?)

<<<<<<< HEAD
    // Update password for a specific user
    @Query("""
        UPDATE users
        SET password = :newPassword
        WHERE email = :identifier OR phone = :identifier
    """)
    suspend fun updatePassword(identifier: String, newPassword: String)

=======
>>>>>>> 88db1f2a0092c7120b833fb021438c1510210e02
    // Optional: Fetch all users (useful for admin/debug purposes)
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    // Clear all users (useful for testing/debugging)
    @Query("DELETE FROM users")
    suspend fun clearAllUsers()
}
package com.sultonbek1547.passportgenerator.database

import androidx.room.*

@Dao
interface MyUserDao {

    // Define a method to get all the users sorted by their position.
    @Query("SELECT * FROM user ORDER BY position ASC")
    fun getAllUsers(): List<User>

    @Delete
    fun deleteUser(user: User)

    @Insert
    fun addUser(user: User)

    @Query("SELECT * FROM user WHERE passportId LIKE '%' || :str || '%'")
    fun checkForPassportId(str: String): List<User>

    @Query("SELECT MAX(position) FROM user")
    fun getMaxPosition(): Int

    @Update
    fun updateUser(user: User)


}
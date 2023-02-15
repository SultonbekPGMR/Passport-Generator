package com.sultonbek1547.passportgenerator.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyUserDao {

    @Query("select * from user")
    fun gelAllUser(): List<User>

    @Delete
    fun deleteUser(user: User)

    @Insert
    fun addUser(user: User)

    @Query("SELECT * FROM user WHERE passportId LIKE '%' || :str || '%'")
    fun checkForPassportId(str: String): List<User>


}
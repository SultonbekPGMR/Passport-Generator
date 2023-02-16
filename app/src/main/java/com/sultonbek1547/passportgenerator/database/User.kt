package com.sultonbek1547.passportgenerator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo
    var name: String? = null

    @ColumnInfo
    var surname: String? = null

    @ColumnInfo
    var passportId: String? = null

    @ColumnInfo
    var photoPath: String? = null

    @ColumnInfo
    var position: Int? = null
    override fun toString(): String {
        return "User(id=$id, name=$name, surname=$surname, passportId=$passportId, photoPath=$photoPath, position=$position)"
    }


}



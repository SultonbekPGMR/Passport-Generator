package com.sultonbek1547.passportgenerator.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User {

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    @ColumnInfo
    var name:String?= null

    @ColumnInfo
    var surname:String?= null

    @ColumnInfo
    var passportId:String?= null


    constructor(id: Int?, name: String?, surname: String?, passportId: String?) {
        this.id = id
        this.name = name
        this.surname = surname
        this.passportId = passportId
    }

    constructor(name: String?, surname: String?, passportId: String?) {
        this.name = name
        this.surname = surname
        this.passportId = passportId
    }

    constructor()


}
package com.sultonbek1547.passportgenerator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sultonbek1547.passportgenerator.database.AppDatabase
import com.sultonbek1547.passportgenerator.database.User
import com.sultonbek1547.passportgenerator.databinding.ActivityAddBinding
import com.sultonbek1547.passportgenerator.utils.MyObject.ADDED
import com.sultonbek1547.passportgenerator.utils.MyObject.USER
import kotlin.random.Random

class AddActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddBinding.inflate(layoutInflater) }
    private lateinit var appDatabase: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)
        var name = ""
        var surname = ""
        binding.apply {
            btnSave.setOnClickListener {
                name = edtName.text.toString().trim()
                surname = edtSurname.text.toString().trim()

                /** saving */
                if (name.isNotEmpty() && surname.isNotEmpty()) {
                    USER = User(name, surname, generatePassportId())
                    appDatabase.myUserDao().addUser(USER!!)
                    ADDED = true
                    finish()
                }


            }

        }


    }


    private fun generatePassportId(): String {
        val result = randomLetter() + randomNumber()
        return if (appDatabase.myUserDao().checkForPassportId(result).isEmpty()) {
            result
        } else {
            generatePassportId()
        }
    }

    fun randomNumber(): String {
        var result = ""
        for (i in 0..6) {
            result += Random.nextInt(10)
        }

        return result
    }

    fun randomLetter(): String {
        var result = ""
        result += Char(Random.nextInt(65, 91))
        result += Char(Random.nextInt(65, 91))

        return result
    }


}
package com.sultonbek1547.passportgenerator

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sultonbek1547.passportgenerator.adapter.RvAdapter
import com.sultonbek1547.passportgenerator.database.AppDatabase
import com.sultonbek1547.passportgenerator.database.User
import com.sultonbek1547.passportgenerator.databinding.ActivityMainBinding
import com.sultonbek1547.passportgenerator.utils.MyObject.ADDED
import com.sultonbek1547.passportgenerator.utils.MyObject.USER

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var appDatabase: AppDatabase
    private lateinit var rvAdapter: RvAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)

        rvAdapter = RvAdapter(this, appDatabase.myUserDao().gelAllUser() as ArrayList<User>)
        binding.myRv.adapter = rvAdapter

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))

        }


    }

    override fun onStart() {
        super.onStart()
        if (ADDED) {
            ADDED = false
            rvAdapter.list.add(USER!!)
            rvAdapter.notifyItemInserted(rvAdapter.itemCount - 1)
        }

    }

}
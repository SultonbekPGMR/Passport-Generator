package com.sultonbek1547.passportgenerator

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.sultonbek1547.passportgenerator.adapter.RvAdapter
import com.sultonbek1547.passportgenerator.database.AppDatabase
import com.sultonbek1547.passportgenerator.database.User
import com.sultonbek1547.passportgenerator.databinding.ActivityMainBinding
import com.sultonbek1547.passportgenerator.utils.MyObject.ADDED
import com.sultonbek1547.passportgenerator.utils.MyObject.USER
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var appDatabase: AppDatabase
    private lateinit var rvAdapter: RvAdapter
    private lateinit var deletedTempUser: User
    var isDeleted = false
    var position = 0
  private  lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        appDatabase = AppDatabase.getInstance(this)

        rvAdapter = RvAdapter(this, appDatabase.myUserDao().getAllUsers() as ArrayList<User>)

        if (rvAdapter.list.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE
        }

        binding.myRv.adapter = rvAdapter
        ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.myRv)

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))

        }


    }

    private var simpleCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
        ItemTouchHelper.RIGHT.or(ItemTouchHelper.LEFT)
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {

            var user = rvAdapter.list[viewHolder.adapterPosition]
            user.position = target.adapterPosition
            appDatabase.myUserDao().updateUser(user)

            user = rvAdapter.list[target.adapterPosition]
            user.position = viewHolder.adapterPosition
            appDatabase.myUserDao().updateUser(user)


            Collections.swap(
                rvAdapter.list,
                viewHolder.adapterPosition,
                target.adapterPosition
            )

            rvAdapter.notifyItemMoved(
                viewHolder.adapterPosition,
                target.adapterPosition
            )



            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            position = viewHolder.adapterPosition
            file = File(
                this@MainActivity.filesDir,
                "${rvAdapter.list[position].id}.jpg"
            )
            deletedTempUser = rvAdapter.list[position]
            rvAdapter.list.removeAt(position)
            rvAdapter.notifyItemRemoved(position)
            isDeleted = true

            if (rvAdapter.list.isEmpty()) {
                binding.tvNoData.visibility = View.VISIBLE
            }

            Snackbar.make(binding.myRv, "${deletedTempUser.name} is deleted", Snackbar.LENGTH_LONG)
                .setAction("Undo") {
                    isDeleted = false
                    rvAdapter.list.add(position, deletedTempUser)
                    rvAdapter.notifyItemInserted(position)
                    if (rvAdapter.list.isNotEmpty()) {
                        binding.tvNoData.visibility = View.GONE
                    }


                }.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        if (isDeleted) {
                            appDatabase.myUserDao().deleteUser(deletedTempUser)
                            file.delete()
                        }

                    }
                }).show()


        }
    }


    override fun onStart() {
        super.onStart()
        if (ADDED) {
            ADDED = false
            rvAdapter.list.add(USER!!)
            rvAdapter.notifyItemInserted(rvAdapter.itemCount - 1)
        }
        if (rvAdapter.list.isNotEmpty()) {
            binding.tvNoData.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        if (isDeleted) {
            appDatabase.myUserDao().deleteUser(deletedTempUser)
            file.delete()
        }
    }

}
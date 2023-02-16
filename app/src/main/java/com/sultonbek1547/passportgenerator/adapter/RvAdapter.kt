package com.sultonbek1547.passportgenerator.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sultonbek1547.passportgenerator.database.User
import com.sultonbek1547.passportgenerator.databinding.RvItemBinding

class RvAdapter(val context: Context, val list: ArrayList<User>) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(val itemRvBinding: RvItemBinding) : RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(user: User, position: Int) {
            itemRvBinding.apply {
                tvName.text = "${user.name} ${user.surname}"
                tvNumber.text = user.passportId
                image.setImageURI(Uri.parse(user.photoPath))
                tvName.setOnClickListener {
                    Toast.makeText(context, "$user", Toast.LENGTH_SHORT).show()
                }

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(list[position], position)


    override fun getItemCount(): Int = list.size


}
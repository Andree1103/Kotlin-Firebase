package com.cibertec.firebaseapp10.ui.dashboard

import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cibertec.firebaseapp10.R
import com.cibertec.firebaseapp10.databinding.ItemUserBinding
import com.cibertec.firebaseapp10.ui.model.User

class UserAdapter constructor(var users:List<User> = listOf()) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    inner class ViewHolder constructor(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val binding : ItemUserBinding = ItemUserBinding.bind(itemView)

        fun bind(user:User){
            binding.tvName.text = user.names
            binding.tvLastName.text = user.lastName
            binding.tvAge.text = "${user.age}"
        }
    }
    fun updateList(users : List<User>){
        this.users= users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_user,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
    }
}
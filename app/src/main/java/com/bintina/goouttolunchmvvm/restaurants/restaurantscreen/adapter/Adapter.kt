package com.bintina.goouttolunchmvvm.restaurants.restaurantscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintina.goouttolunchmvvm.databinding.ItemAttendingBinding
import com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter
import com.bintina.goouttolunchmvvm.restaurants.restaurantscreen.OnAttendingClickedListener
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.loadImage

class Adapter: RecyclerView.Adapter<com.bintina.goouttolunchmvvm.restaurants.restaurantscreen.adapter.Adapter.ItemViewHolder>() {
    var attendingList: List<LocalUser?> = listOf()

    class ItemViewHolder(private val view: ItemAttendingBinding):RecyclerView.ViewHolder(view.root){

        fun bind(user: LocalUser?){

            val profilePictureUrl = user?.profilePictureUrl
            if (profilePictureUrl != null && profilePictureUrl.isNotEmpty()){
                loadImage(profilePictureUrl, view.tvAttendingProfilePhoto)

            }

            val userName = user?.displayName
            if (userName != null && userName.isNotEmpty()){
                view.tvAttendingText.text = "$userName is joining!"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): com.bintina.goouttolunchmvvm.restaurants.restaurantscreen.adapter.Adapter.ItemViewHolder {
        val binding = ItemAttendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = attendingList.size

    override fun onBindViewHolder(holder: com.bintina.goouttolunchmvvm.restaurants.restaurantscreen.adapter.Adapter.ItemViewHolder, position: Int) {
        holder.bind(attendingList[position])
    }

}
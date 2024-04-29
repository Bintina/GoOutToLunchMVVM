package com.bintina.goouttolunchmvvm.user.coworkers.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.ItemCoworkersBinding
import com.bintina.goouttolunchmvvm.user.model.User
import com.bumptech.glide.Glide


class Adapter(): RecyclerView.Adapter<Adapter.ItemViewHolder>() {

    val coworkerList = MyApp.coworkerList
    class ItemViewHolder(private val view: ItemCoworkersBinding, private val context: Context): RecyclerView.ViewHolder(view.root){

        fun bind(coworker: User){
            val userId = coworker.userId
            

          //Load User picture
          val userImageUrl = coworker.profilePicture
            Glide.with(view.ivWorkmateAvatar.context)
                .load(userImageUrl)
                .placeholder(R.drawable.ic_baseline_person_outline_24)
                .centerCrop()
                .into(view.ivWorkmateAvatar)

            //Fetch name
            val coworkerName = coworker.name.toString()


            val coworkerRestaurantChoiceContent = "${coworker.name} is going to Restaurant Choice"
            view.tvWorkmateRestaurantChoice.text = coworkerRestaurantChoiceContent

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemCoworkersBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Log.d("CoworkerAdapterLog", "Coworker adapter onCreateViewHolder called")
        Log.d("CoworkerAdapterLog", "result size = ${coworkerList.size}")
        return ItemViewHolder(
            binding,
            parent.context
        )
    }

    override fun getItemCount(): Int = coworkerList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(coworkerList[position])
    }
}
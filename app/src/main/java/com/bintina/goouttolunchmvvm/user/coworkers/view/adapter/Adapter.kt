package com.bintina.goouttolunchmvvm.user.coworkers.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.ItemCoworkersBinding
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bumptech.glide.Glide


class Adapter() :
    RecyclerView.Adapter<Adapter.ItemViewHolder>() {

    var coworkerList: List<LocalUser?> = listOf()


    val TAG = "CoworkerAdapterLog"

    class ItemViewHolder(private val view: ItemCoworkersBinding, private val context: Context) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(coworker: LocalUser) {
            Log.d("CoworkerAdapterLog", "Coworker adapter bind called")
            //val userId = coworker.userId

            //Load User picture
            val userImageUrl = coworker.profilePictureUrl
            Log.d("CoworkerAdapterLog", "Coworker adapter profile picture url is $userImageUrl")
            Glide.with(view.ivWorkmateAvatar.context)
                .load(userImageUrl)
                .placeholder(R.drawable.ic_baseline_person_outline_24)
                .centerCrop()
                .into(view.ivWorkmateAvatar)

            //Fetch name
            val coworkerName = coworker.displayName.toString()
            Log.d("CoworkerAdapterLog", "Coworker adapter item display name is $coworkerName")
            view.tvWorkmateName.text = coworkerName

            val restaurantChoice = coworker.attendingString
            val coworkerRestaurantChoiceContent = if (restaurantChoice.isNullOrBlank()){
                "$coworkerName hasn't decided yet"
            } else {
                "$coworkerName is eating at $restaurantChoice"

            }
            view.tvWorkmateRestaurantChoice.text = coworkerRestaurantChoiceContent

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "Coworker adapter onCreateViewHolder called")
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
        Log.d(TAG, "Coworker adapter onBindViewHolder called")
        coworkerList[position]?.let { holder.bind(it) }
    }

   /* fun updateData(newCoworkerList: List<LocalUser?>) {
        coworkerList.clear()
        coworkerList.addAll(newCoworkerList)
        notifyDataSetChanged()
    }*/
}
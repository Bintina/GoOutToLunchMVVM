package com.bintina.goouttolunchmvvm.restaurants.list.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bintina.goouttolunchmvvm.MyApp
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.ItemRestaurantBinding
import com.bintina.goouttolunchmvvm.databinding.RestaurantItemViewBinding
import com.bintina.goouttolunchmvvm.restaurants.list.view.OnRestaurantClickedListener
import com.bintina.goouttolunchmvvm.restaurants.model.Restaurant
import com.bumptech.glide.Glide

class Adapter() : RecyclerView.Adapter<com.bintina.goouttolunchmvvm.restaurants.list.view.adapter.Adapter.ItemViewHolder>() {

    val resaurantList = MyApp.restaurantList
    lateinit var listener: OnRestaurantClickedListener

    class ItemViewHolder(private val view: ItemRestaurantBinding, private val context: Context) :

        RecyclerView.ViewHolder(view.root) {
        /**
         * Binds the restaurant data to the view holder.
         */
        fun bind(restaurant: Restaurant, listener: OnRestaurantClickedListener) {
            val restaurantId = restaurant.restaurantId.toString()
            //clickedRestaurantLink = restaurantId



            //Load image using Glide
            val restaurantImageUrl = restaurant.photoUrl

            Glide.with(view.ivPhotoRestaurant.context)
                .load(restaurantImageUrl)
                .placeholder(R.drawable.hungry_droid)
                .centerCrop()
                .into(view.ivPhotoRestaurant)

            //Set Name
            val restaurantName = restaurant.name
            view.tvRestaurantName.text = restaurantName

            //Set Location and type
            view.tvStyleAndAddress.text = restaurantName


            //Set Caption View
            view.tvOpeningHours.text = restaurantName

            //Set click listener for News link
            view.restaurantItem.setOnClickListener { listener.onRestaurantClick() }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Log.d("RestaurantAdapterLog", "Restaurant adapter onCreateViewHolder called")
        Log.d("RestaurantAdapterLog","result size = ${resaurantList.size}")
        return ItemViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(resaurantList[position], listener)
    }

    override fun getItemCount(): Int = resaurantList.size

}
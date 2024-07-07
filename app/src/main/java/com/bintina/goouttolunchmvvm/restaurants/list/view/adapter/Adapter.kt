package com.bintina.goouttolunchmvvm.restaurants.list.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bintina.goouttolunchmvvm.utils.MyApp
import com.bintina.goouttolunchmvvm.R
import com.bintina.goouttolunchmvvm.databinding.ItemRestaurantBinding
import com.bintina.goouttolunchmvvm.restaurants.list.view.OnRestaurantClickedListener
import com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant
import com.bintina.goouttolunchmvvm.user.model.LocalUser
import com.bintina.goouttolunchmvvm.utils.convertRawUrlToUrl
import com.bintina.goouttolunchmvvm.utils.loadImage
import com.bumptech.glide.Glide

class Adapter : RecyclerView.Adapter<Adapter.ItemViewHolder>() {


    var restaurantList: List<LocalRestaurant?> = mutableListOf()
    lateinit var listener: OnRestaurantClickedListener

    class ItemViewHolder(private val view: ItemRestaurantBinding, private val context: Context) :

        RecyclerView.ViewHolder(view.root) {
        /**
         * Binds the restaurant data to the view holder.
         */
        fun bind(
            restaurant: com.bintina.goouttolunchmvvm.restaurants.model.LocalRestaurant?,
            listener: OnRestaurantClickedListener
        ) {

            //val restaurantId = restaurant?.place_id.toString()
            //clickedRestaurantLink = restaurantId


            //Load image using Glide

            try {
                val restaurantImageUrl = restaurant?.photoUrl
                if (restaurantImageUrl != null && restaurantImageUrl.isNotEmpty()) {
                    //Log.d("AdapterLog", "Photo URL: $restaurantImageUrl")

                }
                //Log.d("AdapterLog", "url is $restaurantImageUrl")
                if (restaurantImageUrl != null) {

                    loadImage(restaurantImageUrl, view.ivPhotoRestaurant)
                } else {
                    //Log.d("AdapterLog", "No photo URL generated.")
                }
            } catch (e: Exception) {
                Log.e("RestScreenAdapterLog", "Error converting restaurant to local restaurant", e)
            }

            //Set Name
            val restaurantName = restaurant?.name
            view.tvRestaurantName.text = restaurantName

            //Set Location and type
            /*            val restaurantVicinity = restaurant.vicinity*/
            val restaurantVicinity = restaurant?.address
            view.tvStyleAndAddress.text = restaurantVicinity

            val numberAttending = "(${restaurant?.attending})"
            view.tvNumberOfGuests.text = numberAttending

            /* //Set Caption View
             val restaurantOpen = if (restaurant.opening_hours.open_now == true) {
                 "Open"
             } else {
                 "Closed"
             }
             view.tvOpeningHours.text = restaurantOpen*/

            //Set click listener for News link
            Log.d("RestScreenAdapterLog", "Restaurant is $restaurant")
            view.restaurantItem.setOnClickListener { listener.onRestaurantClick(restaurant!!) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //Log.d("RestaurantAdapterLog", "Restaurant adapter onCreateViewHolder called")
        //Log.d("RestaurantAdapterLog", "result size = ${restaurantList.size}")
        return ItemViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(restaurantList[position], listener)
        //Log.d("RestListAdapterLog", "adapter restaurantList has ${MyApp.restaurantList.size} items")
    }

    override fun getItemCount(): Int = restaurantList.size

}
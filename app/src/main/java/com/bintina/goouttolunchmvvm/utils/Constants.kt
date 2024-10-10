package com.bintina.goouttolunchmvvm.utils

import com.bintina.goouttolunchmvvm.BuildConfig.MAPS_API_KEY

object Constants {


    //Places url
    const val PLACE_BASE_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/"
    const val PLACE_END_URL ="json?keyword=restaurant&location=-4.3015359%2C39.5744260&radius=1500&type=restaurant&key=${MAPS_API_KEY}"

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json
    //  ?keyword=cruise
    //  &location=-33.8670522%2C151.1957362
    //  &radius=1500
    //  &type=restaurant
    //  &key=YOUR_API_KEY
    //Geolocate urls
    const val GEOLOCATE_BASE_URL = "https://www.googleapis.com/geolocation/v1/"
    const val GEOLOCATE_END_URL ="geolocate?key=${MAPS_API_KEY}"

    const val AUTOCOMPLETE_BASE_URL = "https://places.googleapis.com/v1/places:autocomplete"

}

package com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses

data class RestaurantResult(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Restaurant>,
    val status: String
)
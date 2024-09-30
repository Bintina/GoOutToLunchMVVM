package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete

import com.google.android.libraries.places.api.model.AutocompletePrediction

sealed class PlacesSearchEvent

data object PlacesSearchEventLoading : PlacesSearchEvent()

data class PlacesSearchEventError(
    val exception: Throwable
) : PlacesSearchEvent()

data class PlacesSearchEventFound(
    val places: List<AutocompletePrediction>
) : PlacesSearchEvent()
package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.net

import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest

/**
 * Builds a new [FindCurrentPlaceRequest].
 *
 * @param placeFields the fields of the places to be returned
 * @param actions the actions to apply to the [FindCurrentPlaceRequest.Builder]
 *
 * @return the constructed [FindCurrentPlaceRequest]
 */
public fun findCurrentPlaceRequest(
    placeFields: List<Place.Field>,
    actions: (FindCurrentPlaceRequest.Builder.() -> Unit)? = null
): FindCurrentPlaceRequest {
    return FindCurrentPlaceRequest.builder(placeFields).also { builder ->
        actions?.let { builder.apply(it) }
    }.build()
}
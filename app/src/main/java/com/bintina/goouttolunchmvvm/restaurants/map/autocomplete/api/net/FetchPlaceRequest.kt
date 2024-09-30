package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.net

import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest

/**
 * Builds a new [FetchPlaceRequest].
 *
 * @param placeId the ID of the place to fetch
 * @param placeFields the fields of the place to be requested
 * @param actions the actions to apply to the [FetchPlaceRequest.Builder]
 *
 * @return the constructed [FetchPlaceRequest]
 */
public fun fetchPlaceRequest(
    placeId: String,
    placeFields: List<Place.Field>,
    actions: (FetchPlaceRequest.Builder.() -> Unit)? = null
): FetchPlaceRequest {
    return FetchPlaceRequest.builder(placeId, placeFields).also { builder ->
        actions?.let { builder.apply(it) }
    }.build()
}
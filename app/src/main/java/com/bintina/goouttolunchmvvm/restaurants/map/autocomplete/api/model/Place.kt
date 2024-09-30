package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.model

import com.google.android.libraries.places.api.model.Place

/**
 * Builds a new [Place].
 *
 * @param actions the actions to apply to the [Place.Builder]
 *
 * @return the constructed [Place]
 */
public inline fun place(actions: Place.Builder.() -> Unit): Place =
    Place.builder()
        .apply(actions)
        .build()
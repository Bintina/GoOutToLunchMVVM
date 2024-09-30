package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.model

import com.google.android.libraries.places.api.model.OpeningHours

/**
 * Builds a new [OpeningHours].
 *
 * @param actions the actions to apply to the [OpeningHours.Builder]
 *
 * @return the constructed [OpeningHours]
 */
public inline fun openingHours(actions: OpeningHours.Builder.() -> Unit): OpeningHours =
    OpeningHours.builder()
        .apply(actions)
        .build()
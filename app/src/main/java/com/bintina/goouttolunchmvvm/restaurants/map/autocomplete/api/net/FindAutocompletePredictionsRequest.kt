package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.net

import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

/**
 * Builds a new [FindAutocompletePredictionsRequest].
 *
 * @param actions the actions to apply to the [FindAutocompletePredictionsRequest.Builder]
 *
 * @return the constructed [FindAutocompletePredictionsRequest]
 */
public inline fun findAutocompletePredictionsRequest(
    actions: FindAutocompletePredictionsRequest.Builder.() -> Unit
): FindAutocompletePredictionsRequest =
    FindAutocompletePredictionsRequest.builder()
        .apply(actions)
        .build()
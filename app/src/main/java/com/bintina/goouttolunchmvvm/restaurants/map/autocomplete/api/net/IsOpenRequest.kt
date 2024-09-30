package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.net

import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.IsOpenRequest

/**
 * Builds a new [IsOpenRequest].
 *
 * @param placeId The [Place.id] of the place for which isOpen is to be determined.
 * @param utcTimeMillis The milliseconds from 1970-01-01T00:00:00Z.
 * @param actions the actions to apply to the [IsOpenRequest.Builder]
 * @return the constructed [IsOpenRequest]
 */
public fun isOpenRequest(
    placeId: String,
    utcTimeMillis: Long? = null,
    actions: (IsOpenRequest.Builder.() -> Unit)? = null,
): IsOpenRequest {
    return if (utcTimeMillis == null) {
        IsOpenRequest.builder(placeId)
    } else {
        IsOpenRequest.builder(placeId, utcTimeMillis)
    }.also { request ->
        actions?.let { request.apply(it) }
    }.build()
}

/**
 * Builds a new [IsOpenRequest].
 *
 * @param place The [Place] for which isOpen is to be determined.
 * @param utcTimeMillis The milliseconds from 1970-01-01T00:00:00Z.
 * @param actions the actions to apply to the [IsOpenRequest.Builder]
 * @return the constructed [IsOpenRequest]
 * @throws IllegalArgumentException if [Place] does not have a [Place.id] associated with it.
 */
public fun isOpenRequest(
    place: Place,
    utcTimeMillis: Long? = null,
    actions: (IsOpenRequest.Builder.() -> Unit)? = null,
): IsOpenRequest {
    return if (utcTimeMillis == null) {
        IsOpenRequest.builder(place)
    } else {
        IsOpenRequest.builder(place, utcTimeMillis)
    }.also { request ->
        actions?.let { request.apply(it) }
    }.build()
}
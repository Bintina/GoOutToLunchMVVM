package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.net

import com.google.android.libraries.places.api.model.PhotoMetadata
import com.google.android.libraries.places.api.net.FetchPhotoRequest

/**
 * Builds a new [FetchPhotoRequest].
 *
 * @param photoMetadata the metadata for the requested photo
 * @param actions the actions to apply to the [FetchPhotoRequest.Builder]
 *
 * @return the constructed [FetchPhotoRequest]
 */
public fun fetchPhotoRequest(
    photoMetadata: PhotoMetadata,
    actions: (FetchPhotoRequest.Builder.() -> Unit)? = null
): FetchPhotoRequest {
    return FetchPhotoRequest.builder(photoMetadata).also { builder ->
        actions?.let { builder.apply(it) }
    }.build()
}
package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.model

import com.google.android.libraries.places.api.model.PhotoMetadata

/**
 * Builds a new [PhotoMetadata].
 *
 * @param photoReference the reference identifying the underlying photo
 * @param actions the actions to apply to the [PhotoMetadata.Builder]
 *
 * @return the constructed [PhotoMetadata]
 */
public fun photoMetadata(
    photoReference: String,
    actions: (PhotoMetadata.Builder.() -> Unit)? = null
): PhotoMetadata {
    return PhotoMetadata.builder(photoReference).also { builder ->
        actions?.let { builder.apply(it) }
    }.build()
}
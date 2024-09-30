package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.model

import com.google.android.libraries.places.api.model.AddressComponent

/**
 * Builds a new [AddressComponent].
 *
 * @param name the name of this address component
 * @param types the types of this address component
 * @param actions the actions to apply to the [AddressComponent.Builder]
 * @see <a href="https://developers.google.com/maps/documentation/geocoding/intro#Types">Address types</a>
 *
 * @exception IllegalStateException if [name] or [types] is empty
 *
 * @return the constructed [AddressComponent]
 */
public fun addressComponent(
    name: String,
    types: List<String>,
    actions: (AddressComponent.Builder.() -> Unit)? = null
): AddressComponent {
    return AddressComponent.builder(name, types).also { builder ->
        actions?.let { builder.apply(it) }
    }.build()
}
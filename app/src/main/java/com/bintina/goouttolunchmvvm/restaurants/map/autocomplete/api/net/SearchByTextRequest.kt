package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.api.net

import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.SearchByTextRequest

public enum class PriceLevel(public val value: Int) {
    FREE(0),  // Note: not for use in the API call.
    INEXPENSIVE(1),
    MODERATE(2),
    EXPENSIVE(3),
    VERY_EXPENSIVE(4)
}

/**
 * Builds a new [SearchByTextRequest].
 *
 * @param textQuery the query string to search
 * @param placeFields the fields of the place to be requested
 * @param actions the actions to apply to the [SearchByTextRequest.Builder]
 * @return the constructed [SearchByTextRequest]
 */
public fun searchByTextRequest(
    textQuery: String,
    placeFields: List<Place.Field>,
    actions: SearchByTextRequest.Builder.() -> Unit = {},
): SearchByTextRequest {
    return SearchByTextRequest.builder(textQuery, placeFields)
        .apply(actions)
        .build()
}

public fun SearchByTextRequest.Builder.setPriceLevels(
    priceLevels: Collection<PriceLevel>
): SearchByTextRequest.Builder {
    return this.setPriceLevels(priceLevels.map { it.value })
}

public fun SearchByTextRequest.Builder.setPriceLevels(
    vararg priceLevels: PriceLevel
): SearchByTextRequest.Builder {
    return this.setPriceLevels(priceLevels.map { it.value })
}
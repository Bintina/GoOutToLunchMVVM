package com.bintina.goouttolunchmvvm.restaurants.map.view

import android.app.Activity
import androidx.annotation.StringRes
import com.bintina.goouttolunchmvvm.R
import com.google.android.libraries.places.widget.AutocompleteActivity

enum class Demo(
    @StringRes val title: Int,
    @StringRes val description: Int,
    val activity: Class<out Activity>
) {
    AUTOCOMPLETE_FRAGMENT_DEMO(
        R.string.autocomplete_fragment_demo_title,
        R.string.autocomplete_fragment_demo_description,
        AutocompleteActivity::class.java
    ),
    PLACES_SEARCH_DEMO(
        R.string.places_demo_title,
        R.string.places_demo_description,
        PlacesSearchActivity::class.java
    )
}
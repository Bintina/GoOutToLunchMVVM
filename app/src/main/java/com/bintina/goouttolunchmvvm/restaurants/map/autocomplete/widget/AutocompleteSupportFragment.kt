package com.bintina.goouttolunchmvvm.restaurants.map.autocomplete.widget

import android.util.Log
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

public sealed class PlaceSelectionResult

public data class PlaceSelectionSuccess(val place: Place) : PlaceSelectionResult()

public data class PlaceSelectionError(val status: Status) : PlaceSelectionResult()

public fun AutocompleteSupportFragment.placeSelectionEvents() : Flow<PlaceSelectionResult> =
    callbackFlow {
        this@placeSelectionEvents.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                trySend(PlaceSelectionSuccess(place))
                Log.d("AutocompleteSupportFragLog", "onPlaceSelected called")
            }

            override fun onError(status: Status) {
                trySend(PlaceSelectionError(status))
                Log.d("AutocompleteSupportFragLog", "onError called, status: $status")
            }
        })
        awaitClose { this@placeSelectionEvents.setOnPlaceSelectedListener(null) }
    }
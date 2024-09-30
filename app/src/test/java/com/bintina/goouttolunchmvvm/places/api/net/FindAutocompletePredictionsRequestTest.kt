/*
package com.bintina.goouttolunchmvvm.places.api.net

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.model.PlaceTypes
import com.google.android.libraries.places.api.model.RectangularBounds
import org.junit.Assert.assertEquals
import org.junit.Test

internal class FindAutocompletePredictionsRequestTest {

    @Test
    fun testBuilder() {
        val cancellationToken = CancellationTokenSource().token
        val request = findAutocompletePredictionsRequest {
            setCancellationToken(cancellationToken)
            setCountries("USA")
            locationBias = RectangularBounds.newInstance(LatLng(1.0,1.0), LatLng(2.0, 2.0))
            typesFilter = listOf(PlaceTypes.ESTABLISHMENT)
            query = "query"
        }
        assertEquals(cancellationToken, request.cancellationToken)
        assertEquals(listOf("USA"), request.countries)
        assertEquals(
            RectangularBounds.newInstance(LatLng(1.0,1.0), LatLng(2.0, 2.0)),
            request.locationBias
        )
        assertEquals(listOf(PlaceTypes.ESTABLISHMENT), request.typesFilter)
        assertEquals("query", request.query)
    }
}*/

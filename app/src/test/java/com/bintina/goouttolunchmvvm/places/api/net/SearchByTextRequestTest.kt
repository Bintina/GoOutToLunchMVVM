/*
package com.bintina.goouttolunchmvvm.places.api.net

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.SearchByTextRequest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class SearchByTextRequestTest {
    @Test
    fun testBuilderNoActions() {
        val request = searchByTextRequest(
            textQuery = "test query",
            placeFields = listOf(Place.Field.NAME)
        )

        assertThat(request.textQuery).isEqualTo("test query")
        assertThat(request.placeFields).containsExactly(Place.Field.NAME)
    }

    @Test
    fun testBuilderWithActions() {
        val cancellationToken = CancellationTokenSource().token
        val ashland = LatLng(42.193893370553916, -122.7088890892941)
        val radiusInMeters = 1500.0

        val request = searchByTextRequest(
            textQuery = "test query",
            placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS),
        ) {
            setCancellationToken(cancellationToken)
            includedType = "national_park"
            isOpenNow = true
            minRating = 4.0
            setPriceLevels(PriceLevel.MODERATE, PriceLevel.EXPENSIVE)
            rankPreference = SearchByTextRequest.RankPreference.RELEVANCE
            regionCode = "US"

            locationBias = CircularBounds.newInstance(ashland, radiusInMeters)
        }

        assertThat(request.locationBias)
            .isEqualTo(CircularBounds.newInstance(ashland, radiusInMeters))
        assertThat(request.isOpenNow).isTrue()
        assertThat(request.minRating).isEqualTo(4.0)
        assertThat(request.priceLevels)
            .containsExactly(PriceLevel.MODERATE.value, PriceLevel.EXPENSIVE.value)
        assertThat(request.rankPreference).isEqualTo(SearchByTextRequest.RankPreference.RELEVANCE)
        assertThat(request.regionCode).isEqualTo("US")
        assertThat(request.includedType).isEqualTo("national_park")
        assertThat(request.textQuery).isEqualTo("test query")
    }
}*/

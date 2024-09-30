/*
package com.bintina.goouttolunchmvvm.places.api.net

import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.libraries.places.api.model.Place
import org.junit.Assert.assertEquals
import org.junit.Test

internal class FindCurrentPlaceRequestTest {

    @Test
    fun testBuilderNoActions() {
        val request = findCurrentPlaceRequest(listOf(Place.Field.NAME))
        assertEquals(listOf(Place.Field.NAME), request.placeFields)
    }

    @Test
    fun testBuilderWithActions() {
        val cancellationToken = CancellationTokenSource().token
        val request = findCurrentPlaceRequest(listOf(Place.Field.NAME)) {
            setCancellationToken(cancellationToken)
        }
        assertEquals(listOf(Place.Field.NAME), request.placeFields)
        assertEquals(cancellationToken, request.cancellationToken)
    }
}*/

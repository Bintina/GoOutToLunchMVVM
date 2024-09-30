/*
package com.bintina.goouttolunchmvvm.places.model

import com.google.android.libraries.places.api.model.AddressComponents
import com.google.android.libraries.places.ktx.api.model.addressComponent
import com.google.android.libraries.places.ktx.api.model.place
import org.junit.Assert.assertEquals
import org.junit.Test

internal class PlaceTest {

    @Test
    fun testBuilder() {
        val place = place {
            address = "address"
            addressComponents = AddressComponents.newInstance(
                listOf(
                    addressComponent("Main Street", listOf("street_address")) {
                        shortName = "Main St."
                    }
                )
            )
        }
        assertEquals("address", place.address)
        assertEquals(AddressComponents.newInstance(
            listOf(
                addressComponent("Main Street", listOf("street_address")) {
                    shortName = "Main St."
                }
            )
        ), place.addressComponents)
    }
}*/

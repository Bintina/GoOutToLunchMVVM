/*package com.bintina.goouttolunchmvvm.places.model

import com.google.android.libraries.places.ktx.api.model.addressComponent
import org.junit.Assert.assertEquals
import org.junit.Test

internal class AddressComponentTest {

    @Test
    fun testBuilderNoShortName() {
        val component = addressComponent(
            "Main Street",
            listOf("street_address")
        )
        assertEquals("Main Street", component.name)
        assertEquals(listOf("street_address"), component.types)
    }

    @Test
    fun testBuilderWithShortName() {
        val component = addressComponent(
            "Main Street",
            listOf("street_address")
        ) {
            shortName = "Main St."
        }
        assertEquals("Main Street", component.name)
        assertEquals("Main St.", component.shortName)
        assertEquals(listOf("street_address"), component.types)
    }
}*/

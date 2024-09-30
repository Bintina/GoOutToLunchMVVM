/*
package com.bintina.goouttolunchmvvm.places.model

import com.google.android.libraries.places.api.model.DayOfWeek
import com.google.android.libraries.places.api.model.LocalTime
import com.google.android.libraries.places.api.model.TimeOfWeek
import com.google.android.libraries.places.ktx.api.model.period
import org.junit.Assert.assertEquals
import org.junit.Test

internal class PeriodTest {

    @Test
    fun testBuilder() {
        val period = period {
            close = TimeOfWeek.newInstance(
                DayOfWeek.MONDAY,
                LocalTime.newInstance(0, 0)
            )
            open = TimeOfWeek.newInstance(
                DayOfWeek.TUESDAY,
                LocalTime.newInstance(0, 0)
            )
        }
        assertEquals(
            TimeOfWeek.newInstance(
                DayOfWeek.MONDAY,
                LocalTime.newInstance(0, 0)
            ),
            period.close
        )
        assertEquals(
            TimeOfWeek.newInstance(
                DayOfWeek.TUESDAY,
                LocalTime.newInstance(0, 0)
            ),
            period.open
        )
    }
}*/

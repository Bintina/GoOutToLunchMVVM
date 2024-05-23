package com.bintina.goouttolunchmvvm.mock.repository

import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Geometry
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Location
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Northeast
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.OpeningHours
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Photo
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.PlusCode
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Restaurant
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Southwest
import com.bintina.goouttolunchmvvm.restaurants.model.database.responseclasses.Viewport

data class MockRestaurants(override val size: Int) : List<Restaurant> {

    val mockRestaurantList: List<Restaurant> by lazy {
        generateMockRestaurants()
    }

    private fun generateMockRestaurants(): List<Restaurant> {
        //create a list of mock Restaurant Objects
        return listOf(
            Restaurant(
                "OPERATIONAL",
                Geometry(
                    Location(
                        -4.308492,
                        39.5808522
                    ),
                    Viewport(
                        Northeast(
                            -4.306733170107277,
                            39.58215147989272
                        ),
                        Southwest(
                            -4.309432829892721,
                            39.57945182010727
                        )
                    )
                ),
                "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
                "#FF9E67",
                "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
                "Asha Bistro",
                OpeningHours(
                    true
                ),
                listOf(
                    Photo(
                        3008,
                        listOf(
                            "<a href=\"https://maps.google.com/maps/contrib/100116944439636469333\">Thomas Kößler</a>"
                        ),
                        "AUGGfZkKawz04Mn8TNvMynPtqT20ioVV-fv2QhchDKbyX4Zx4fbzVENja7JG6aND8IfuO8VCY7LnGYXJUlb3iiNJJ6aPXwZd1m0LtDNuwnHuVpvnSnNpq4YXv35HlQx6I9N8QVZfp3zHpNCtfI_xlK-Iws1l0LjE5zrfVKHSlg2jKhLJ31rn",
                        4000
                    )
                ),
                "ChIJBQZH9UhJQBgRqKDlUOBtZz8",
                PlusCode(
                    "MHRJ+J8 Diani Beach",
                    "6G7XMHRJ+J8"
                ),
                2,
                4.6,
                "ChIJBQZH9UhJQBgRqKDlUOBtZz8",
                "GOOGLE",
                listOf(
                    "restaurant",
                    "food",
                    "point_of_interest",
                    "establishment"
                ),
                328,
                "Diani Beach"
            ),
            Restaurant(
                "OPERATIONAL",
                Geometry(
                    Location(
                        -4.3000374,
                        39.5844083
                    ),
                    Viewport(
                        Northeast(
                            -4.298428420107278,
                            39.58518432989272
                        ),
                        Southwest(
                            -4.301128079892722,
                            39.58248467010728
                        )
                    )
                ),
                "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
                "#FF9E67",
                "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
                "Chill Spot",
                OpeningHours(
                    false
                ),
                listOf(
                    Photo(
                        1200,
                        listOf(
                            "<a href=\"https://maps.google.com/maps/contrib/104116616813854986679\">A Google User</a>"
                        ),
                        "AUGGfZkUdFe9Ym6B0XK43FWC3bvgEexRXZuAewfxxb-94GMSQzybTyI07FygwBHNq_IsbbvX2VHKkTU1UpBHW4XUHW2iDJmAM4yoY5P_zI6TvBI1XdDYcJCXtLefrl8r7jLKv_YOfcxCoC4rgc4TbKocU4XDx9VwnWNP9Cqa0Hy6CzhM9NLy",
                        1600
                    )
                ),
                "ChIJidxM6kFJQBgR_eR4ffbhZ24",
                PlusCode(
                    "MHXM+XQ Diani Beach",
                    "6G7XMHXM+XQ"
                ),
                2,
                4.4,
                "ChIJidxM6kFJQBgR_eR4ffbhZ24",
                "GOOGLE",
                listOf(
                    "restaurant",
                    "food",
                    "point_of_interest",
                    "establishment"
                ),
                185,
                "Diani Shopping bazaar, Diani Beach"
            ),
            Restaurant(
                "OPERATIONAL",
                Geometry(
                    Location(
                        -4.3037114,
                        39.58172769999999
                    ),
                    Viewport(
                        Northeast(
                            -4.302396270107278,
                            39.58306407989272
                        ),
                        Southwest(
                            -4.305095929892722,
                            39.58036442010727
                        )
                    )
                ),
                "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
                "#FF9E67",
                "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
                "SPEEDY'S BISTRO",
                OpeningHours(
                    false
                ),
                listOf(

                    Photo(
                        809,
                        listOf(
                            "<a href=\"https://maps.google.com/maps/contrib/108419134947801369344\">A Google User</a>"
                        ),
                        "AUGGfZndBYUKutoGXPtcyo_VLQxQTdrgEQA-i0LTZuo59pGp2_zgMnJ_5g_MneCaCmYGrLvrqwYszBws_bzt3nQVPbzENJV7VqeFuGfPOgY5bGEf46G7skQOTYKIIjAAfEovj0p0v0hG6-dmae7kXxVqgOMecBLYF6lT1IBkCfWE5BaV1yAb",
                        1440
                    )
                ),
                "ChIJc3I72ShJQBgRFjDlo8LO21U",
                PlusCode(
                    "MHWJ+GM Diani Beach",
                    "6G7XMHWJ+GM"
                ),
                0,
                4.7,
                "ChIJc3I72ShJQBgRFjDlo8LO21U",
                "GOOGLE",
                listOf(
                    "restaurant",
                    "food",
                    "point_of_interest",
                    "establishment"
                ),
                26,
                "Tradewinds"
            ),
            Restaurant(
                "OPERATIONAL",
                Geometry(
                    Location(

                        -4.2877395,
                        39.58468610000001
                    ),
                    Viewport(
                        Northeast(
                            -4.286250870107278,
                            39.58607827989272
                        ),
                        Southwest(
                            -4.288950529892722,
                            39.58337862010728
                        )
                    ),
                ),
                "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/restaurant-71.png",
                "#FF9E67",
                "https://maps.gstatic.com/mapfiles/place_api/icons/v2/restaurant_pinlet",
                "SPEEDY'S BISTRO",
                OpeningHours(
                    true
                ),
                listOf(
                    Photo(
                        4160,
                        listOf(
                            "<a href=\"https://maps.google.com/maps/contrib/108442764760890658732\">Alice Muthoni</a>"
                        ),
                        "AUGGfZkg8J-nPQqwdXMmP8VCfg5FN8lmybDRLvoseA7a_eOaK2CKe_7un9LRr3_iYL9CphyMoEBw8Tn3I4NLw7fxBaoBfSbdKfHZcFuq5SeXotBcBpJeTpJdp1K1L2Z2oBf2H9lxuFurLpgkz3C--Jw-hcoETEDGcoSUxn4-k9XKBxsklepW",
                        3120
                    )
                ),

                "ChIJh3OGZKVHQBgRZo1j68lLvM4",
                PlusCode(
                    "",
                    ""
                ),
                0,
                5.00,
                "ChIJh3OGZKVHQBgRZo1j68lLvM4",
                "GOOGLE",
                listOf(
                    "restaurant",
                    "food",
                    "point_of_interest",
                    "establishment"
                ),
                1,
                "PH6M+WV3, Tradewinds, Diani Beach"
            )

        )
    }


    override fun contains(element: Restaurant): Boolean {
        return mockRestaurantList.contains(element)
    }

    override fun containsAll(elements: Collection<Restaurant>): Boolean {
        return mockRestaurantList.containsAll(elements)
    }

    override fun get(index: Int): Restaurant {
        return mockRestaurantList[index]
    }

    override fun isEmpty(): Boolean {
        return mockRestaurantList.isEmpty()
    }

    override fun iterator(): Iterator<Restaurant> {
        return mockRestaurantList.iterator()
    }

    override fun listIterator(): ListIterator<Restaurant> {
        return mockRestaurantList.listIterator()
    }

    override fun listIterator(index: Int): ListIterator<Restaurant> {
        return mockRestaurantList.listIterator(index)
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<Restaurant> {
        return mockRestaurantList.subList(fromIndex, toIndex)
    }

    override fun lastIndexOf(element: Restaurant): Int {
        return mockRestaurantList.lastIndexOf(element)
    }

    override fun indexOf(element: Restaurant): Int {
        return mockRestaurantList.indexOf(element)
    }
}

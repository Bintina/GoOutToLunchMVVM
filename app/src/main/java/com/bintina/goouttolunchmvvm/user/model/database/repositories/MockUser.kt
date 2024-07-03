package com.bintina.goouttolunchmvvm.user.model.database.repositories

import com.bintina.goouttolunchmvvm.user.model.LocalUser
import java.time.LocalDateTime
import java.time.ZoneOffset

data class MockUser(override val size: Int): List<LocalUser>{

    val mockLocalUserList: List<LocalUser?> by lazy {
        generateMockUsers()
    }

    private fun generateMockUsers(): List<LocalUser?> {
        // Dummy date and time: December 31, 2022, 23:59:59
        val dummyUpdateDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59)
        val updatedAt = dummyUpdateDate.toEpochSecond(ZoneOffset.UTC).toLong()
        // Dummy date and time: December 31, 2023, 23:59:59
        val dummyCreatedDate = LocalDateTime.of(2021, 12, 31, 23, 59, 59)
        val createdAt = dummyCreatedDate.toEpochSecond(ZoneOffset.UTC).toLong()
        //Create a list of mock User objects
        return listOf(
            LocalUser("10", "Albert", "albert@example.com",  "https://picsum.photos/seed/picsum/200/200","Alfredos", createdAt, updatedAt),
            LocalUser("11", "Bernard","bernard@example.com",  "https://picsum.photos/seed/picsum/200/200", "Bernardos", createdAt, updatedAt),
            LocalUser("12", "Charles", "charles@example.com", "https://picsum.photos/seed/picsum/200/200", "Carnivor", createdAt, updatedAt),
            LocalUser("13", "Derrick", "derrick@example.com",  "https://picsum.photos/seed/picsum/200/200", "Dinos", createdAt, updatedAt)
        )
    }

    override fun contains(element: LocalUser): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<LocalUser>): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(index: Int): LocalUser {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<LocalUser> {
        TODO("Not yet implemented")
    }

    override fun listIterator(): ListIterator<LocalUser> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<LocalUser> {
        TODO("Not yet implemented")
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<LocalUser> {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: LocalUser): Int {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: LocalUser): Int {
        TODO("Not yet implemented")
    }
}

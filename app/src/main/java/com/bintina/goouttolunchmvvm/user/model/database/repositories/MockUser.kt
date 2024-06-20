package com.bintina.goouttolunchmvvm.user.model.database.repositories

import com.bintina.goouttolunchmvvm.user.model.LocalUser

data class MockUser(override val size: Int): List<LocalUser>{

    val mockLocalUserList: List<LocalUser?> by lazy {
        generateMockUsers()
    }

    private fun generateMockUsers(): List<LocalUser?> {
        //Create a list of mock User objects
        return listOf(
            LocalUser("10", "Albert", "albert@example.com",  "https://picsum.photos/seed/picsum/200/200"),
            LocalUser("11", "Bernard","bernard@example.com",  "https://picsum.photos/seed/picsum/200/200"),
            LocalUser("12", "Charles", "charles@example.com", "https://picsum.photos/seed/picsum/200/200"),
            LocalUser("13", "Derrick", "derrick@example.com",  "https://picsum.photos/seed/picsum/200/200")
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

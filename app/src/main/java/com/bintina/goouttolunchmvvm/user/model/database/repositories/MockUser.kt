package com.bintina.goouttolunchmvvm.user.model.database.repositories

import com.bintina.goouttolunchmvvm.user.model.User

data class MockUser(override val size: Int): List<User>{

    val mockUserList: List<User?> by lazy {
        generateMockUsers()
    }

    private fun generateMockUsers(): List<User?> {
        //Create a list of mock User objects
        return listOf(
            User("10", "Albert", "albert@example.com",  "https://picsum.photos/seed/picsum/200/200"),
            User("11", "Bernard","bernard@example.com",  "https://picsum.photos/seed/picsum/200/200"),
            User("12", "Charles", "charles@example.com", "https://picsum.photos/seed/picsum/200/200"),
            User("13", "Derrick", "derrick@example.com",  "https://picsum.photos/seed/picsum/200/200")
        )
    }

    override fun contains(element: User): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements: Collection<User>): Boolean {
        TODO("Not yet implemented")
    }

    override fun get(index: Int): User {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }

    override fun iterator(): Iterator<User> {
        TODO("Not yet implemented")
    }

    override fun listIterator(): ListIterator<User> {
        TODO("Not yet implemented")
    }

    override fun listIterator(index: Int): ListIterator<User> {
        TODO("Not yet implemented")
    }

    override fun subList(fromIndex: Int, toIndex: Int): List<User> {
        TODO("Not yet implemented")
    }

    override fun lastIndexOf(element: User): Int {
        TODO("Not yet implemented")
    }

    override fun indexOf(element: User): Int {
        TODO("Not yet implemented")
    }
}

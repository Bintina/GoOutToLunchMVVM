package com.bintina.goouttolunchmvvm.login.model

data class User(val userId: Int,
                val name: String,
                val loginState: Int,
                val profilePicture: String,
                var restaurant: String?)

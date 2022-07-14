package com.example.mystoryapp.tools

object Matcher {
    fun emailValid(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}
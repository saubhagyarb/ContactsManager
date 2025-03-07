package com.example.contactsmanager

class Contact(
    @JvmField val id: Int,
    @JvmField val name: String?,
    val mobile: String?,
    val email: String?,
    val address: String?
)

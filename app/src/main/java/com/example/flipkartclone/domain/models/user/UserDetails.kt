package com.example.flipkartclone.domain.models.user

data class UserDetails(
    var firstName: String? = null,
    var lastName: String? = null,
    var userId: String? = null,
    var phoneNumber: String? = null,
    var gender: String? = null,
    var address: Address? = null
)
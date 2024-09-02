package com.example.flipkartclone.domain.models

data class Review(
    val rating: Int,
    val review: String,
    val username: String,
    val reviewImages: String
)
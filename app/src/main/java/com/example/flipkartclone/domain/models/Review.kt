package com.example.flipkartclone.domain.models

import java.io.Serializable

data class Review(
    val rating: Int,
    val review: String,
    val username: String,
    val reviewImages: String
):Serializable
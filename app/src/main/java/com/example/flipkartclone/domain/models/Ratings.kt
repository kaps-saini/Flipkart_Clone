package com.example.flipkartclone.domain.models

data class Ratings(
    val average: Double,
    val count: Int,
    val details: Details?,
    val distribution: Distribution
)
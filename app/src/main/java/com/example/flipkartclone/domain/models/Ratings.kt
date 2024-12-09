package com.example.flipkartclone.domain.models

import java.io.Serializable

data class Ratings(
    val average: Double,
    val count: Int,
    val details: Details?,
    val distribution: Distribution
):Serializable
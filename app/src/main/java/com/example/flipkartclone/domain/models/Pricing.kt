package com.example.flipkartclone.domain.models

import java.io.Serializable

data class Pricing(
    val discount: Int,
    val mrp: Int,
    val sellingPrice: Int
):Serializable
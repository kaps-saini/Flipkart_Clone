package com.example.flipkartclone.domain.models

import java.io.Serializable

data class ItemModelItem(
    val availableQuantity: Int,
    val category: String,
    val desc: String,
    val discountedPrice: Double,
    val id: Int,
    val images: List<String>,
    val price: Double,
    val ratings: Double,
    val ratingsCount: Int,
    val title: String
)
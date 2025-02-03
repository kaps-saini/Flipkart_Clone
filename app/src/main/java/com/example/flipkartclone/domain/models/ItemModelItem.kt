package com.example.flipkartclone.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class ItemModelItem(
    val availableQuantity: Int,
    val category: String,
    val desc: String,
    val discountedPrice: Double,
    @PrimaryKey
    val id: Int,
    val images: List<String>,
    val price: Double,
    val ratings: Double,
    val ratingsCount: Int,
    val title: String
)
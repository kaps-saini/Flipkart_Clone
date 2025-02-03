package com.example.flipkartclone.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "droppedItem")
data class ItemDataModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val highlights: List<Highlight>,
    val item: Item,
    val pricing: Pricing,
    val ratings: Ratings,
    val reviews: List<Review>?
):Serializable
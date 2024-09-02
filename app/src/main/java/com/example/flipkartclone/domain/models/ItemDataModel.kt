package com.example.flipkartclone.domain.models

import java.io.Serializable

data class ItemDataModel(
    val highlights: List<Highlight>,
    val item: Item,
    val pricing: Pricing,
    val ratings: Ratings,
    val reviews: List<Review>?
):Serializable
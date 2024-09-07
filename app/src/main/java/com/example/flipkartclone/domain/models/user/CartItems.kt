package com.example.flipkartclone.domain.models.user

import com.example.flipkartclone.domain.models.Item
import com.example.flipkartclone.domain.models.Pricing
import com.example.flipkartclone.domain.models.Ratings

data class CartItems(
    val item: Item? = null,
    val pricing: Pricing? = null,
    val ratings: Ratings? = null,
    var quantity: Int = 1
)
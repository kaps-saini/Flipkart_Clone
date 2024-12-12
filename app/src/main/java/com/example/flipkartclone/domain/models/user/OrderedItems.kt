package com.example.flipkartclone.domain.models.user

import com.example.flipkartclone.domain.models.Item
import com.example.flipkartclone.domain.models.Pricing
import com.example.flipkartclone.domain.models.Ratings
import java.io.Serializable

data class OrderedItems(
    val orderId:String,
    val paymentId:String,
    val item: Item? = null,
    val price: Int,
    val ratings: Ratings? = null,
    var quantity: Int = 0,
    val billingAmount:Int = 0,
    val discount:Int = 0,
    val userAddress: String,
    val userContact:String
):Serializable

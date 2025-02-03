package com.example.flipkartclone.domain.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Product(
    @SerializedName("brand")
    val brand: String?,
    @SerializedName("category")
    val category: String?,
    @SerializedName("color")
    val color: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("discount")
    val discount: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("model")
    val model: String?,
    @SerializedName("onSale")
    val onSale: Boolean?,
    @SerializedName("popular")
    val popular: Boolean?,
    @SerializedName("price")
    val price: Int?,
    @SerializedName("title")
    val title: String?
) : Parcelable
package com.example.flipkartclone.domain.models


import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class FakeStore(
    @SerializedName("message")
    val message: String?,
    @SerializedName("products")
    val products: List<Product?>?,
    @SerializedName("status")
    val status: String?
) : Parcelable
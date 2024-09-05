package com.example.flipkartclone.domain.models.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    var name: String? = null,
    var phone: String? = null,
    var pincode: String? = null,
    var state: String? = null,
    var city: String? = null,
    var houseNo: String? = null,
    var area: String? = null,
    var type: String? = null, // home or work
    var location: String? = null
):Parcelable

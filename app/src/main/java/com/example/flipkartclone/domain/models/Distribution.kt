package com.example.flipkartclone.domain.models

import java.io.Serializable

data class Distribution(
    val `1star`: Int,
    val `2star`: Int,
    val `3star`: Int,
    val `4star`: Int,
    val `5star`: Int
):Serializable
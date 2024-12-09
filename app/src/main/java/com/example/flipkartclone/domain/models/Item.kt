package com.example.flipkartclone.domain.models

import java.io.Serializable

data class Item(
    val category: String,
    val description: String,
    val id: String,
    val images: List<String>,
    val name: String
):Serializable
package com.example.flipkartclone.domain.models

data class Item(
    val category: String,
    val description: String,
    val id: String,
    val images: List<String>,
    val name: String
)
package com.example.flipkartclone.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "brandsForYou")
data class BrandsForYou(
    @PrimaryKey
    val id:Int,
    val title:String,
    val images:List<String>
)

package com.example.flipkartclone.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("sponsor_table")
data class Sponsor(
    @PrimaryKey
    val id:Int,
    val title:String,
    val images:List<String>
)

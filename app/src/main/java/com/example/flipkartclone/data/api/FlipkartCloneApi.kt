package com.example.flipkartclone.data.api

import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
import retrofit2.Response
import retrofit2.http.GET

interface FlipkartCloneApi {

    @GET("/explore")
    suspend fun getItems():Response<List<ItemModelItem>>

    @GET("/justDropped")
    suspend fun getJustDroppedItemData():Response<List<ItemDataModel>>
}
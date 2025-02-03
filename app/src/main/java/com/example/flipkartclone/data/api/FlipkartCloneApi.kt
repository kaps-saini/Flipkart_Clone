package com.example.flipkartclone.data.api

import com.example.flipkartclone.domain.models.BrandsForYou
import com.example.flipkartclone.domain.models.FakeStore
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.domain.models.Sponsor
import retrofit2.Response
import retrofit2.http.GET

interface FlipkartCloneApi {

    @GET("/explore")
    suspend fun getItems():Response<List<ItemModelItem>>

    @GET("/brandsForYou")
    suspend fun getBrandsForYou():Response<List<BrandsForYou>>

    @GET("/justDropped")
    suspend fun getJustDroppedItemData():Response<List<ItemDataModel>>

    @GET("/sponsors")
    suspend fun getSponsorsData():Response<List<Sponsor>>
}
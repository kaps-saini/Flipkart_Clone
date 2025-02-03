package com.example.flipkartclone.data.api

import com.example.flipkartclone.domain.models.FakeStore
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FakeStoreApi {
    @GET("api/products")
    suspend fun getFakeProducts(
        @Query("page") page:Int,
        @Query("limit") limit:Int
    ): FakeStore
}
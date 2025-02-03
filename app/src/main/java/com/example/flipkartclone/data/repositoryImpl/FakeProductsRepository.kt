package com.example.flipkartclone.data.repositoryImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.flipkartclone.data.api.FakeStoreApi
import com.example.flipkartclone.data.pagingSource.FakeProductsPagingSource
import com.example.flipkartclone.domain.models.FakeStore
import com.example.flipkartclone.domain.models.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FakeProductsRepository @Inject constructor(
    private val fakeStoreApi: FakeStoreApi
) {
    fun getFakeProducts(): Flow<PagingData<Product>>{
        return Pager(
            config = PagingConfig(
                pageSize = 3,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {FakeProductsPagingSource(fakeStoreApi)}
        ).flow
    }
}
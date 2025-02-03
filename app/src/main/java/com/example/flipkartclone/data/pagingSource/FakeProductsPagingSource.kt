package com.example.flipkartclone.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.flipkartclone.data.api.FakeStoreApi
import com.example.flipkartclone.domain.models.FakeStore
import com.example.flipkartclone.domain.models.Product
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class FakeProductsPagingSource @Inject constructor(
    private val fakeStoreApi: FakeStoreApi
):PagingSource<Int,Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val page = params.key?:1
        return try {
            val response = fakeStoreApi.getFakeProducts(page, params.loadSize)
            val products = response.products?.filterNotNull() ?: emptyList()
            LoadResult.Page(
                data = products,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (products.isEmpty()) null else page + 1
            )
        }catch (e:IOException){
            LoadResult.Error(e)
        }catch (e:HttpException){
            LoadResult.Error(e)
        }
    }
}
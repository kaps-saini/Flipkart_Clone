package com.example.flipkartclone.data.repositoryImpl

import com.example.flipkartclone.data.api.FlipkartCloneApi
import com.example.flipkartclone.domain.interfaces.Repository
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ItemsRepositoryImpl @Inject constructor(
    private val flipkartCloneApi: FlipkartCloneApi
):Repository {
    override suspend fun getItemsData(): Flow<Resource<List<ItemModelItem>>> = flow {
        emit(Resource.Loading())
       try {
            val response = flipkartCloneApi.getItems()
            if (response.isSuccessful && response.body() != null){
                emit(Resource.Success(response.body()!!))
            }else{
                emit(Resource.Error(response.message()))
            }
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getJustDropped(): Flow<Resource<List<ItemDataModel>>> = flow {
        emit(Resource.Loading())
        try {
            val response = flipkartCloneApi.getJustDroppedItemData()
            if (response.isSuccessful && response.body() != null){
                emit(Resource.Success(response.body()!!))
            }else{
                emit(Resource.Error(response.message()))
            }
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}
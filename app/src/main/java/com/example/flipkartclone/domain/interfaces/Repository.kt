package com.example.flipkartclone.domain.interfaces

import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getItemsData(): Flow<Resource<List<ItemModelItem>>>

    suspend fun getJustDropped():Flow<Resource<List<ItemDataModel>>>

}
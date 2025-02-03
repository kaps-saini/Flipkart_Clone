package com.example.flipkartclone.domain.interfaces

import com.example.flipkartclone.domain.models.BrandsForYou
import com.example.flipkartclone.domain.models.FakeStore
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.domain.models.Sponsor
import com.example.flipkartclone.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun getItemsData(): Flow<Resource<List<ItemModelItem>>>

    suspend fun getExploreItems():Flow<Resource<List<ItemModelItem>>>

    suspend fun getBrandsForYou():Flow<Resource<List<BrandsForYou>>>

    suspend fun getJustDropped():Flow<Resource<List<ItemDataModel>>>

    suspend fun getSponsors():Flow<Resource<List<Sponsor>>>
}
package com.example.flipkartclone.data.repositoryImpl

import android.app.Application
import com.example.flipkartclone.data.api.FakeStoreApi
import com.example.flipkartclone.data.api.FlipkartCloneApi
import com.example.flipkartclone.data.cache.BrandsForYouDao
import com.example.flipkartclone.data.cache.DroppedItemDao
import com.example.flipkartclone.data.cache.ItemDao
import com.example.flipkartclone.data.cache.SponsorsDao
import com.example.flipkartclone.domain.interfaces.Repository
import com.example.flipkartclone.domain.models.BrandsForYou
import com.example.flipkartclone.domain.models.FakeStore
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.domain.models.Sponsor
import com.example.flipkartclone.utils.CheckNetwork
import com.example.flipkartclone.utils.Resource
import com.example.flipkartclone.utils.Status
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemsRepositoryImpl @Inject constructor(
    private val flipkartCloneApi: FlipkartCloneApi,
    private val itemDao: ItemDao,
    private val droppedItemDao: DroppedItemDao,
    private val brandsForYouDao: BrandsForYouDao,
    private val sponsorsDao: SponsorsDao,
    val app: Application,
    private val network: CheckNetwork,
):Repository {
    override suspend fun getItemsData(): Flow<Resource<List<ItemModelItem>>> = flow {
        emit(Resource.Loading())
       try {
           if (network.hasInternetConnection(app)){
               val response = flipkartCloneApi.getItems()
               if (response.isSuccessful && response.body() != null){
                   itemDao.clearAll()
                   itemDao.insertAll(response.body()!!)

                   val cacheData = itemDao.getAllItems()
                   emitAll(cacheData.map { item->
                       Resource.Success(item)
                   })
               }else{
                   emit(Resource.Error(response.message()))
               }
           }else{
               val cacheData = itemDao.getAllItems()
               emitAll(cacheData.map { item->
                   Resource.Success(item)
               })
           }
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getExploreItems(): Flow<Resource<List<ItemModelItem>>> = flow{
        emit(Resource.Loading())
        try {
            if (network.hasInternetConnection(app)){
                val response = flipkartCloneApi.getItems()
                if (response.isSuccessful && response.body() != null){
                    emit(Resource.Success(response.body()!!))
                }else{
                    emit(Resource.Error(response.message()))
                }
            }else{
               emit(Resource.Error(Status.NoInternet.toString()))
            }
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getBrandsForYou(): Flow<Resource<List<BrandsForYou>>> = flow {
        emit(Resource.Loading())
        try {
            if (network.hasInternetConnection(app)){
                val response = flipkartCloneApi.getBrandsForYou()
                if (response.isSuccessful && response.body() != null){
                    brandsForYouDao.clearAll()
                    brandsForYouDao.insert(response.body()!!)

                    val cacheData = brandsForYouDao.getAllBrandsList()
                    emitAll(cacheData.map { item->
                        Resource.Success(item)
                    })
                }else{
                    emit(Resource.Error(response.message()))
                }
            }else{
                val cacheData = brandsForYouDao.getAllBrandsList()
                emitAll(cacheData.map { item->
                    Resource.Success(item)
                })
            }
        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getJustDropped(): Flow<Resource<List<ItemDataModel>>> = flow {
        emit(Resource.Loading())
        try {
            if (network.hasInternetConnection(app)){
                val response = flipkartCloneApi.getJustDroppedItemData()
                if (response.isSuccessful && response.body() != null){
                    droppedItemDao.clearAll()
                    droppedItemDao.insertAll(response.body()!!)

                    val cache = droppedItemDao.getAllDroppedItems()
                    emitAll(cache.map { item ->
                        Resource.Success(item)
                    })
                }else{
                    emit(Resource.Error(response.message()))
                }
            }else{
                val cache = droppedItemDao.getAllDroppedItems()
                emitAll(cache.map { item ->
                    Resource.Success(item)
                })
            }

        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }

    override suspend fun getSponsors(): Flow<Resource<List<Sponsor>>>  = flow {
        emit(Resource.Loading())
        try {
            if (network.hasInternetConnection(app)){
                val response = flipkartCloneApi.getSponsorsData()
                if (response.isSuccessful && response.body() != null){
                    sponsorsDao.clearAll()
                    sponsorsDao.insertAll(response.body()!!)

                    val cache = sponsorsDao.getAllSponsors()
                    emitAll(cache.map { item ->
                        Resource.Success(item)
                    })
                }else{
                    emit(Resource.Error(response.message()))
                }
            }else{
                val cache = sponsorsDao.getAllSponsors()
                emitAll(cache.map { item ->
                    Resource.Success(item)
                })
            }

        }catch (e:Exception){
            emit(Resource.Error(e.message.toString()))
        }
    }
}
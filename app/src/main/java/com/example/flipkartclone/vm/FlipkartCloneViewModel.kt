package com.example.flipkartclone.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.flipkartclone.data.repositoryImpl.FakeProductsRepository
import com.example.flipkartclone.data.user.OrderedItemPref
import com.example.flipkartclone.data.user.UserCartItemsPref
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.domain.interfaces.Repository
import com.example.flipkartclone.domain.models.BrandsForYou
import com.example.flipkartclone.domain.models.FakeStore
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.domain.models.Product
import com.example.flipkartclone.domain.models.Sponsor
import com.example.flipkartclone.domain.models.user.Address
import com.example.flipkartclone.domain.models.user.CartItems
import com.example.flipkartclone.utils.CheckNetwork
import com.example.flipkartclone.utils.Resource
import com.example.flipkartclone.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlipkartCloneViewModel @Inject constructor(
    private val repository: Repository,
    fakeProductsRepository: FakeProductsRepository,
    val app: Application,
    private val network: CheckNetwork,
    private val userDetailsPref: UserDetailsPref,
    private val userCartItemsPref: UserCartItemsPref,
    private val userOrderedItemPref: OrderedItemPref
) : ViewModel() {

    private var _itemsResult = MutableStateFlow<Resource<List<ItemModelItem>>>(Resource.Loading())
    val itemResult: StateFlow<Resource<List<ItemModelItem>>>
        get() = _itemsResult
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Resource.Loading()
            )

    private var _brandsForYou = MutableStateFlow<Resource<List<BrandsForYou>>>(Resource.Loading())
    val brandsForYou: StateFlow<Resource<List<BrandsForYou>>>
        get() = _brandsForYou
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Resource.Loading()
            )

    private var _sponsors = MutableStateFlow<Resource<List<Sponsor>>>(Resource.Loading())
    val sponsors: StateFlow<Resource<List<Sponsor>>>
        get() = _sponsors
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Resource.Loading()
            )

    private var _exploreResult = MutableStateFlow<Resource<List<ItemModelItem>>>(Resource.Loading())
    val exploreResult: StateFlow<Resource<List<ItemModelItem>>>
        get() = _exploreResult
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Resource.Loading()
            )

    private var _droppedItems = MutableStateFlow<Resource<List<ItemDataModel>>>(Resource.Loading())
    val droppedItems: StateFlow<Resource<List<ItemDataModel>>> get() = _droppedItems

    private var _addressData: MutableLiveData<List<Address>?> = MutableLiveData()
    val addressData: LiveData<List<Address>?> get() = _addressData

    private var _cartItemsData: MutableLiveData<List<CartItems>?> = MutableLiveData()
    val cartItemsData: LiveData<List<CartItems>?> get() = _cartItemsData

    private var _orderedItemData: MutableLiveData<List<CartItems>?> = MutableLiveData()
    val orderedItemData: LiveData<List<CartItems>?> get() = _orderedItemData

    val fakeProducts: Flow<PagingData<Product>> = fakeProductsRepository.getFakeProducts().cachedIn(viewModelScope)

    init {
        getDroppedItems()
        getBrandsForYou()
        getAllItems()
        getSponsors()
    }

    private fun getBrandsForYou() {
        viewModelScope.launch {
            repository.getBrandsForYou().collect { result ->
                _brandsForYou.value = result
            }
        }
    }

    private fun getSponsors() {
        viewModelScope.launch {
            repository.getSponsors().collect { result ->
                _sponsors.value = result
            }
        }
    }

    private fun getAllItems() {
        viewModelScope.launch {
            repository.getItemsData().collect { result ->
                _itemsResult.value = result
            }
        }
    }

    fun getExploreItems() {
        viewModelScope.launch {
            repository.getExploreItems().collect { result ->
                _exploreResult.value = result
            }
        }
    }

    private fun getDroppedItems() {
        viewModelScope.launch {
            repository.getJustDropped().collect { result ->
                _droppedItems.value = result
            }
        }
    }

    fun getAddress() {
        val data = userDetailsPref.getAddressListAsLive()
        _addressData.value = data
    }

    fun getItemsInCart() {
        val data = userCartItemsPref.getCartListAsLive()
        _cartItemsData.value = data
    }

}
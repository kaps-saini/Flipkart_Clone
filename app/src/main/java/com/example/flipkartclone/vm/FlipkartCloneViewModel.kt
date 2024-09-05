package com.example.flipkartclone.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flipkartclone.data.user.UserDetailsPref
import com.example.flipkartclone.domain.interfaces.Repository
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.domain.models.user.Address
import com.example.flipkartclone.utils.CheckNetwork
import com.example.flipkartclone.utils.Resource
import com.example.flipkartclone.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlipkartCloneViewModel @Inject constructor(
    private val repository: Repository,
    val app:Application,
    private val network: CheckNetwork,
    private val userDetailsPref: UserDetailsPref
):ViewModel() {

    private var _itemsResult = MutableStateFlow<Resource<List<ItemModelItem>>>(Resource.Loading())
    val itemResult:StateFlow<Resource<List<ItemModelItem>>> get() = _itemsResult

    private var _droppedItems = MutableStateFlow<Resource<List<ItemDataModel>>>(Resource.Loading())
    val droppedItems:StateFlow<Resource<List<ItemDataModel>>> get() = _droppedItems

    private var _addressData: MutableLiveData<List<Address>?> = MutableLiveData()
    val addressData: LiveData<List<Address>?> get() = _addressData

    init {
        getDroppedItems()
    }

    fun getAllItems(){
        viewModelScope.launch {
            if (network.hasInternetConnection(app)){
                _itemsResult.value = Resource.Loading()
                repository.getItemsData().collect{ result ->
                    _itemsResult.value = result
                }
            }else{
                _itemsResult.value = Resource.Error(Status.NoInternet.toString())
            }
        }
    }

    private fun getDroppedItems(){
        viewModelScope.launch {
            if (network.hasInternetConnection(app)){
                _droppedItems.value = Resource.Loading()
                repository.getJustDropped().collect{ result ->
                    _droppedItems.value = result
                }
            }else{
                _droppedItems.value = Resource.Error(Status.NoInternet.toString())
            }
        }
    }

    fun getAddress(){
        val data = userDetailsPref.getAddressListAsLive()
        _addressData.value = data
    }

}
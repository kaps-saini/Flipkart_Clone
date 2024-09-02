package com.example.flipkartclone.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flipkartclone.domain.interfaces.Repository
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
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
    private val network: CheckNetwork
):ViewModel() {

    private var _itemsResult = MutableStateFlow<Resource<List<ItemModelItem>>>(Resource.Loading())
    val itemResult:StateFlow<Resource<List<ItemModelItem>>> get() = _itemsResult

    private var _droppedItems = MutableStateFlow<Resource<List<ItemDataModel>>>(Resource.Loading())
    val droppedItems:StateFlow<Resource<List<ItemDataModel>>> get() = _droppedItems

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

}
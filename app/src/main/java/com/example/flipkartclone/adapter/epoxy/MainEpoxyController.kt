package com.example.flipkartclone.adapter.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.epoxy.helper.ViewBindingKotlinModel
import com.example.flipkartclone.databinding.EpoxyLayoutContentBinding
import com.example.flipkartclone.databinding.SkuListLayoutBinding
import com.google.android.gms.common.util.CollectionUtils.listOf

class MainEpoxyController :EpoxyController(){

    var isLoading:Boolean = false
        set(value){
            field = value
            if(field){
                requestModelBuild()
            }
        }

    var sampleData = listOf<SampleModel>()
        set(value){
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {
       if (isLoading){
           //todo loading state
           return
       }

        if (sampleData.isEmpty()){
            //todo show empty state
            return
        }

        sampleData.forEach{ data->
            CategoryEpoxyModel(data)
                .id(data.id)
                .addTo(this)
        }
    }

    data class CategoryEpoxyModel(
        val sampleModel: SampleModel
    ):ViewBindingKotlinModel<SkuListLayoutBinding>(R.layout.sku_list_layout) {

        override fun SkuListLayoutBinding.bind() {
            tvSkuName.text = sampleModel.content
        }
    }

}
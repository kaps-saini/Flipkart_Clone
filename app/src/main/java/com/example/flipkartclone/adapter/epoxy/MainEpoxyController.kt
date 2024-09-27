package com.example.flipkartclone.adapter.epoxy

import com.airbnb.epoxy.EpoxyController
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.epoxy.helper.ViewBindingKotlinModel
import com.example.flipkartclone.databinding.EpoxyLayoutContentBinding
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
    ):ViewBindingKotlinModel<EpoxyLayoutContentBinding>(R.layout.epoxy_layout_content) {

        override fun EpoxyLayoutContentBinding.bind() {
            tvTitleEpoxy.text = sampleModel.content
        }
    }

}
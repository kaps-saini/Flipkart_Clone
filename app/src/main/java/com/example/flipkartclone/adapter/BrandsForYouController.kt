package com.example.flipkartclone.adapter

import com.airbnb.epoxy.EpoxyController
import com.bumptech.glide.Glide
import com.example.flipkartclone.R
import com.example.flipkartclone.adapter.epoxy.helper.ViewBindingKotlinModel
import com.example.flipkartclone.databinding.BrandsLoveItemBinding
import com.example.flipkartclone.domain.models.ItemModelItem

class BrandsForYouController(
    private val onClickCallback:(item: ItemModelItem) -> Unit
):EpoxyController() {

    var isLoading:Boolean = false
        set(value){
            field = value
            if (field){
                requestModelBuild()
            }
        }

    var brandsData = listOf<ItemModelItem>()
        set(value){
            field = value
            isLoading = false
            requestModelBuild()
        }

    override fun buildModels() {
        if (isLoading){

        }

        if (brandsData.isEmpty()){

        }

        brandsData.forEach{ data->
            brandsForYouModel(data,onClickCallback)
                .id(data.id)
                .addTo(this)
        }
    }

    data class brandsForYouModel(
        private val itemModelItem: ItemModelItem,
        private val onClick:(itemData: ItemModelItem) -> Unit
    ):ViewBindingKotlinModel<BrandsLoveItemBinding>(R.layout.brands_love_item){
        override fun BrandsLoveItemBinding.bind() {
            tvBrandsLoveTitle.text = itemModelItem.title
            Glide.with(this.root.context)
                .load(itemModelItem.images[0])
                .into(ivBrandsForYou)

            root.setOnClickListener{
                onClick(itemModelItem)
            }
        }
    }

}
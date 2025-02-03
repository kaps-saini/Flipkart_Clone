package com.example.flipkartclone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flipkartclone.databinding.ItemGridLayoutBinding
import com.example.flipkartclone.domain.models.Product

class FakeProductAdapter:PagingDataAdapter<Product,FakeProductAdapter.FakeViewHolder>(FAKE_PRODUCT_COMPARATOR) {

    inner class FakeViewHolder(private val binding:ItemGridLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(product: Product){
            val price = product.price.toString()
            binding.tvItemNameExplore.text = product.title
            binding.tvSellingPriceExplore.text = "₹$price"
            binding.tvMrpExplore.text = product.price.toString()
            binding.tvDiscountExplore.text = "40% off"
            binding.tvItemDescExplore.text = product.description
            Glide.with(itemView.context)
                .load(product.image)
                .into(binding.ivItemImage)
            binding.ratings.rating = 4f
        }
    }

    override fun onBindViewHolder(holder: FakeProductAdapter.FakeViewHolder, position: Int) {
        val currentProduct = getItem(position)
        if (currentProduct != null) {
            holder.bind(currentProduct)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FakeViewHolder {
        val view = ItemGridLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return FakeViewHolder(view)
    }

    companion object {
        private val FAKE_PRODUCT_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                oldItem == newItem
        }
    }
}
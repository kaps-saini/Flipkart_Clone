package com.example.flipkartclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flipkartclone.R
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem

class JustDroppedDeals (private val onClick:(position:Int,itemData: ItemDataModel) -> Unit
): RecyclerView.Adapter<JustDroppedDeals.BrandsViewHolder>() {

    inner class BrandsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.ivJustDrppedItemImage)
        val itemTitle: TextView = itemView.findViewById(R.id.tvItemNameDropped)
        val itemOffer: TextView = itemView.findViewById(R.id.tvItemPriceDropped)

        fun bind(itemDataModel: ItemDataModel){
            itemTitle.text = itemDataModel.item.name
            itemOffer.text = itemDataModel.pricing.sellingPrice.toString()
            Glide.with(itemView.context)
                .load(itemDataModel.item.images[0])
                .error(R.drawable.error_image)
                .into(itemImage)
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ItemDataModel>() {
        override fun areItemsTheSame(oldItem: ItemDataModel, newItem: ItemDataModel): Boolean {
            return oldItem.item.id == newItem.item.id
        }

        override fun areContentsTheSame(oldItem: ItemDataModel, newItem: ItemDataModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsViewHolder {
        return BrandsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.dropped_item,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BrandsViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)

        holder.itemView.setOnClickListener {
            onClick(position,currentItem)
        }

    }
}
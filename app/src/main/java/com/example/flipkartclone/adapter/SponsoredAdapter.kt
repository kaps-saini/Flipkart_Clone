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
import com.example.flipkartclone.domain.models.ItemModelItem


class SponsoredAdapter(
    private val onClick:(position:Int,itemData: ItemModelItem) -> Unit
): RecyclerView.Adapter<SponsoredAdapter.BrandsViewHolder>() {

    inner class BrandsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.ivSponsoredImage)
        val itemTitle: TextView = itemView.findViewById(R.id.tvSponsoredTitle)

        fun bind(itemModelItem: ItemModelItem){
            itemTitle.text = itemModelItem.title
            Glide.with(itemView.context)
                .load(itemModelItem.images[0])
                .into(itemImage)
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ItemModelItem>() {
        override fun areItemsTheSame(oldItem: ItemModelItem, newItem: ItemModelItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ItemModelItem, newItem: ItemModelItem): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsViewHolder {
        return BrandsViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.sponsored_item,parent,false))
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
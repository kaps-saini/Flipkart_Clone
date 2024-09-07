package com.example.flipkartclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flipkartclone.R
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem

class RecentlyViewedItems (
    private val onClick:(position:Int,itemData: ItemDataModel) -> Unit,
    private val onAddClick:(position:Int,itemData: ItemDataModel) -> Unit
): RecyclerView.Adapter<RecentlyViewedItems.BrandsViewHolder>() {

    inner class BrandsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.ivAddToCart)
        val itemTitle: TextView = itemView.findViewById(R.id.tvItemTitleInCartAdd)
        val itemSellingPrice: TextView = itemView.findViewById(R.id.tvSellingPriceAddToCart)
        val itemMrp: TextView = itemView.findViewById(R.id.tvMrpInAdd)
        val itemDiscount: TextView = itemView.findViewById(R.id.tvItemDescCartAdd)
        val btnAdd: FrameLayout = itemView.findViewById(R.id.btnAdd)
        val tvAdd: TextView = itemView.findViewById(R.id.tvAddToCart)
        val pbAdd: ProgressBar = itemView.findViewById(R.id.pbAddToCart)

        fun bind(itemDataModel: ItemDataModel){
            val price = itemDataModel.pricing.sellingPrice.toString()
            itemTitle.text = itemDataModel.item.name
            itemSellingPrice.text = "₹$price"
            itemMrp.text = itemDataModel.pricing.mrp.toString()
            itemDiscount.text = "40% off"
            Glide.with(itemView.context)
                .load(itemDataModel.item.images[0])
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
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.add_to_cart_item_layout,parent,false)
        return BrandsViewHolder(view)
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

        holder.btnAdd.setOnClickListener {
            onAddClick(position,currentItem)
            holder.tvAdd.visibility = View.GONE
            holder.pbAdd.visibility = View.VISIBLE
            holder.tvAdd.text = "Added"
            holder.pbAdd.visibility = View.GONE
        }
    }
}
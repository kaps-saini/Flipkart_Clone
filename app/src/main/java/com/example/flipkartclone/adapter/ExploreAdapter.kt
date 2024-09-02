package com.example.flipkartclone.adapter

import android.media.Rating
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flipkartclone.R
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.presentation.fragments.Explore

class ExploreAdapter(private val onClick:(position:Int,itemData: ItemModelItem) -> Unit
): RecyclerView.Adapter<ExploreAdapter.BrandsViewHolder>() {

    inner class BrandsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.ivItemImage)
        val itemTitle: TextView = itemView.findViewById(R.id.tvItemNameExplore)
        val itemDesc: TextView = itemView.findViewById(R.id.tvItemDescExplore)
        val itemSellingPrice: TextView = itemView.findViewById(R.id.tvSellingPriceExplore)
        val itemMrp: TextView = itemView.findViewById(R.id.tvMrpExplore)
        val itemDiscount: TextView = itemView.findViewById(R.id.tvDiscountExplore)
        val itemRating: RatingBar = itemView.findViewById(R.id.ratings)

        fun bind(itemModelItem: ItemModelItem){
            val price = itemModelItem.discountedPrice.toString()
            itemTitle.text = itemModelItem.title
            itemSellingPrice.text = "/$/$price"
            itemMrp.text = itemModelItem.price.toString()
            itemDiscount.text = "40% off"
            itemDesc.text = itemModelItem.desc
            Glide.with(itemView.context)
                .load(itemModelItem.images[0])
                .into(itemImage)
            itemRating.rating = itemModelItem.ratings.toFloat()
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
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_grid_layout,parent,false)

        // Calculate the width of the screen
        val screenWidth = parent.context.resources.displayMetrics.widthPixels

        // Define the number of columns
        val columns = 2

        // Add some margin between items (e.g., 16dp)
        val margin = parent.context.resources.getDimensionPixelSize(R.dimen.explore_margin)

        // Calculate the width of each item (subtracting margins)
        val itemWidth = (screenWidth / columns) - (margin * 2)

        // Set the item width and apply the margin
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = itemWidth
        layoutParams.setMargins(margin, margin, margin, margin)
        view.layoutParams = layoutParams

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

    }
}
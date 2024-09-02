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
import com.example.flipkartclone.domain.models.Highlight
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem

class ProductHighlights (): RecyclerView.Adapter<ProductHighlights.BrandsViewHolder>() {

    inner class BrandsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.ivProductHighLightImage)
        val itemTitle: TextView = itemView.findViewById(R.id.tvProductHighLightDesc)

        fun bind(highlight: Highlight){
            itemTitle.text = highlight.description
            Glide.with(itemView.context)
                .load(highlight.image)
                .error(R.drawable.error_image)
                .into(itemImage)
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Highlight>() {
        override fun areItemsTheSame(oldItem: Highlight, newItem: Highlight): Boolean {
            return oldItem.image == newItem.image
        }

        override fun areContentsTheSame(oldItem: Highlight, newItem: Highlight): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsViewHolder {
        return BrandsViewHolder(
            LayoutInflater.from(parent.context).inflate(
            R.layout.prodcut_highlight_item,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BrandsViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
    }
}
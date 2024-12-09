package com.example.flipkartclone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flipkartclone.adapter.ProductHighlights
import com.example.flipkartclone.adapter.epoxy.SampleModel
import com.example.flipkartclone.domain.models.Highlight

class CategoryAdapter(): RecyclerView.Adapter<CategoryAdapter.BrandsViewHolder>() {

    inner class BrandsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemTitle: TextView = itemView.findViewById(R.id.tvSkuName)

        fun bind(sampleModel: SampleModel){
            itemTitle.text = sampleModel.content
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<SampleModel>() {
        override fun areItemsTheSame(oldItem: SampleModel, newItem: SampleModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SampleModel, newItem: SampleModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsViewHolder {
        return BrandsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.sku_list_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BrandsViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)


    }
}
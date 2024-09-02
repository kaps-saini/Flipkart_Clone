package com.example.flipkartclone.adapter

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
import com.example.flipkartclone.domain.models.Highlight
import com.example.flipkartclone.domain.models.Review

class ProductReviews (): RecyclerView.Adapter<ProductReviews.BrandsViewHolder>() {

    inner class BrandsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.rvReviewImages)
        val itemTitle: TextView = itemView.findViewById(R.id.tvTitleReview)
        val productRating: RatingBar = itemView.findViewById(R.id.rbUserRating)
        val ratingAverage: TextView = itemView.findViewById(R.id.tvUserReviewRating)
        val ratingDesc: TextView = itemView.findViewById(R.id.tvReviewDesc)
        val reviewUserName: TextView = itemView.findViewById(R.id.tvReviewUserName)

        fun bind(reviews: Review){
            itemTitle.text = "Excellent Product"
            productRating.rating = reviews.rating.toFloat()
            ratingAverage.text = reviews.rating.toString()
            ratingDesc.text = reviews.review
            reviewUserName.text = reviews.username
            Glide.with(itemView.context)
                .load(reviews.reviewImages)
                .error(R.drawable.error_image)
                .into(itemImage)
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.reviewImages == newItem.reviewImages
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsViewHolder {
        return BrandsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.review_item,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BrandsViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)
    }
}
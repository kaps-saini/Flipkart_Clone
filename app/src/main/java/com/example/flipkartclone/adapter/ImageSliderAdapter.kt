package com.example.flipkartclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flipkartclone.R

class ImageSliderAdapter(
    private val imageList:List<String>,
    private val onItemClick:(position:Int) -> Unit
):RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    inner class ImageSliderViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val image = view.findViewById<ImageView>(R.id.ivSliding)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        return ImageSliderViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.single_item_image_slider,parent,false))
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val imageUrl = imageList[position]

        // Use Glide to load the image from the URL
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .error(R.drawable.ic_launcher_background) // Optional error image if load fails
            .into(holder.image)

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }
}
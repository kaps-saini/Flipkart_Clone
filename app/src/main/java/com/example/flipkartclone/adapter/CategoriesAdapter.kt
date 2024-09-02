package com.example.flipkartclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.flipkartclone.R
import com.example.flipkartclone.domain.models.CategoriesDataModel

class CategoriesAdapter(
    private val categoriesList: List<CategoriesDataModel>,
    private val onItemClick: (position:Int,title:String) -> Unit
):RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    inner class CategoriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val image:ImageView = itemView.findViewById(R.id.ivCategory)
        val title:TextView = itemView.findViewById(R.id.tvTitle)

        fun bind(categoriesDataModel: CategoriesDataModel){
            title.text = categoriesDataModel.title
            image.setImageResource(categoriesDataModel.categoryImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.category_item,parent,false))
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val currentCategory = categoriesList[position]
        holder.bind(categoriesList[position])

        holder.itemView.setOnClickListener {
            onItemClick(position,currentCategory.title)
        }
    }
}
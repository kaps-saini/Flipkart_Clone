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
import com.example.flipkartclone.domain.models.user.CartItems
import com.example.flipkartclone.domain.models.user.OrderedItems

class Orders(
    private val onClick:(position:Int,itemData: OrderedItems) -> Unit,
):RecyclerView.Adapter<Orders.OrdersViewHolder>() {

    inner class OrdersViewHolder(view:View):RecyclerView.ViewHolder(view){
        private val image = view.findViewById<ImageView>(R.id.ivOrderImage)
        private val itemDesc = view.findViewById<TextView>(R.id.tvItemDesc)

        fun bind(orderedItems: OrderedItems){
            itemDesc.text = orderedItems.item?.description.toString()
            Glide.with(itemView.context)
                .load(orderedItems.item?.images?.get(0))
                .into(image)
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<OrderedItems>() {
        override fun areItemsTheSame(oldItem: OrderedItems, newItem: OrderedItems): Boolean {
            return oldItem.item?.id == newItem.item?.id
        }

        override fun areContentsTheSame(oldItem: OrderedItems, newItem: OrderedItems): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.my_orders_list,parent,false))
    }

    override fun onBindViewHolder(holder: Orders.OrdersViewHolder, position: Int) {
        val currentList = differ.currentList[position]
        holder.bind(currentList)

        holder.itemView.setOnClickListener {
            onClick(position,currentList)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
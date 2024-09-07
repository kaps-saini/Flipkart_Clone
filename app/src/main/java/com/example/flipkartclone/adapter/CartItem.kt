package com.example.flipkartclone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.UnitModelLoader
import com.example.flipkartclone.R
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.domain.models.user.CartItems

class CartItem (
    private val onClick:(position:Int,itemData: CartItems) -> Unit,
    private val onQuantityChange:(itemData:CartItems) -> Unit
): RecyclerView.Adapter<CartItem.BrandsViewHolder>() {

    private val quantities = (1..3).map { it.toString() }

    inner class BrandsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.itemImageInCart)
        val itemTitle: TextView = itemView.findViewById(R.id.tvItemNameCart)
        val itemDesc: TextView = itemView.findViewById(R.id.tvItemDescCart)
        val itemSellingPrice: TextView = itemView.findViewById(R.id.tvSellingPriceCart)
        val itemMrp: TextView = itemView.findViewById(R.id.tvMrpCart)
        val itemDiscount: TextView = itemView.findViewById(R.id.tvDiscountCart)
        val itemRating: RatingBar = itemView.findViewById(R.id.tvRatingCart)
        val itemRatingAvg: TextView = itemView.findViewById(R.id.tvRatingAverageCart)
        val itemRatingCount: TextView = itemView.findViewById(R.id.tvRatingCountCart)
        val quantity:AutoCompleteTextView = itemView.findViewById(R.id.quantityAutoCompleteTextView)
        val btnRemove:TextView = itemView.findViewById(R.id.btnRemove)
        val saveLater:TextView = itemView.findViewById(R.id.tvSaveLater)

        fun bind(cartItems: CartItems){
            val price = cartItems.pricing?.sellingPrice.toString()
            itemTitle.text = cartItems.item?.name
            itemSellingPrice.text = "₹$price"
            itemMrp.text = "₹${cartItems.pricing?.mrp.toString()}"
            val discount = cartItems.pricing?.discount.toString()
            itemDiscount.text = "$discount% off"
            itemDesc.text = cartItems.item?.description.toString()
            Glide.with(itemView.context)
                .load(cartItems.item?.images?.get(0))
                .into(itemImage)
            itemRating.rating = cartItems.ratings?.average?.toFloat()!!
            itemRatingAvg.text = cartItems.ratings.average.toString()
            itemRatingCount.text = cartItems.ratings.count.toString()

            val adapter = ArrayAdapter(itemView.context,android.R.layout.simple_dropdown_item_1line,quantities)
            quantity.setAdapter(adapter)
            quantity.setOnItemClickListener { _, _, position, _ ->
                val selectedQuantity = quantities[position]
                cartItems.quantity = selectedQuantity.toInt()
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<CartItems>() {
        override fun areItemsTheSame(oldItem: CartItems, newItem: CartItems): Boolean {
            return oldItem.item?.id == newItem.item?.id
        }

        override fun areContentsTheSame(oldItem: CartItems, newItem: CartItems): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.cart_item,parent,false)

        return BrandsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BrandsViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)

        holder.btnRemove.setOnClickListener {
            onClick(position,currentItem)
        }


    }
}
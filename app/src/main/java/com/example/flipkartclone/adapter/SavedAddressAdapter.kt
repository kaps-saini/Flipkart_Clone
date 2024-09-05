package com.example.flipkartclone.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flipkartclone.R
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.domain.models.user.Address

class SavedAddressAdapter(
    private val onClick:(position:Int,address: Address) -> Unit,
    private val onRemoveClick:(position:Int,address: Address) -> Unit
): RecyclerView.Adapter<SavedAddressAdapter.BrandsViewHolder>() {

    inner class BrandsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.tvUserNameAddress)
        val userAddressType: TextView = itemView.findViewById(R.id.tvAddressTypeAddress)
        val addressLine1: TextView = itemView.findViewById(R.id.tvHouseAddress)
        val addressLine2: TextView = itemView.findViewById(R.id.tvAreaAddress)
        val addressPhone: TextView = itemView.findViewById(R.id.tvPhoneNumberAddress)
        val ivOption: ImageView = itemView.findViewById(R.id.ivOption)

        fun bind(address: Address){
            with(address) {
                val line1 = "$houseNo $area"
                val line2 = "$city $state $pincode"
                userName.text = address.name
                userAddressType.text = address.type
                addressLine1.text = line1
                addressLine2.text = line2
                addressPhone.text = address.phone
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsViewHolder {
        return BrandsViewHolder(
            LayoutInflater.from(parent.context).inflate(
            R.layout.address_item,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BrandsViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem)

        // Handle options menu click
        holder.ivOption.setOnClickListener {
            // Create a PopupMenu
            val popupMenu = PopupMenu(holder.itemView.context, holder.ivOption)
            popupMenu.inflate(R.menu.recycler_address_menu)

            // Handle menu item clicks
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.edit -> {
                        // Handle edit action
                        onClick(position, currentItem)
                        true
                    }
                    R.id.remove -> {
                        // Handle remove action
                        onRemoveClick(position, currentItem)
                        true
                    }
                    else -> false
                }
            }

            // Show the popup menu
            popupMenu.show()
        }

    }
}
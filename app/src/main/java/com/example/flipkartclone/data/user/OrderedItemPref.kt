package com.example.flipkartclone.data.user

import android.content.Context
import android.content.SharedPreferences
import com.example.flipkartclone.domain.models.user.CartItems
import com.example.flipkartclone.domain.models.user.OrderedItems
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class OrderedItemPref @Inject constructor(
    @ApplicationContext private val context:Context
) {

    private val prefs: SharedPreferences = context.getSharedPreferences("OrderedItems", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()
    private val gson = Gson()

    fun addOrderedItem(newOrderItem: OrderedItems) {
        val orderedList = getOrderedListAsLive()?.toMutableList()
        orderedList?.add(newOrderItem)
        if (orderedList != null) {
            saveOrderedItem(orderedList)
        }
    }

//    fun addOrderedItemList(newOrderItem: List<OrderedItems>) {
//        val orderedItems = getOrderedList().toMutableList()
//        orderedItems.addAll(newOrderItem)
//        saveOrderedItem(orderedItems)
//    }

//    // Delete an Address from the list
//    fun deleteOrderedItem(cartItems: CartItems) {
//        val orderedList = getOrderedList().toMutableList()
//        orderedList.remove(cartItems)
//        saveOrderedItem(orderedList)
//    }
//
    fun clearOrderedItems() {
        editor.remove("OrderedItems")
        editor.apply()
    }

//    // Get the list of Addresses
//    private fun getOrderedList(): List<OrderedItems> {
//        val json = prefs.getString("OrderedList", null)
//        return if (json != null) {
//            val type = object : TypeToken<List<OrderedItems>>() {}.type
//            gson.fromJson(json, type)
//        } else {
//            emptyList()
//        }
//    }

    fun getOrderedListAsLive(): List<OrderedItems>? {
        val json = prefs.getString("OrderedItems", null) ?: return emptyList()
        val type = object : TypeToken<List<OrderedItems>>() {}.type
        return gson.fromJson(json, type)
    }

    // Save the Address list to SharedPreferences
    private fun saveOrderedItem(cartList: List<OrderedItems>) {
        val json = gson.toJson(cartList)
        editor.putString("OrderedItems", json)
        editor.apply()
    }


}
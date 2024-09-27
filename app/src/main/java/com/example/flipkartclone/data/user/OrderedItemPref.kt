package com.example.flipkartclone.data.user

import android.content.Context
import android.content.SharedPreferences
import com.example.flipkartclone.domain.models.user.CartItems
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

    // Add an Address to the list
    fun addOrderedItem(newCartItem: CartItems) {
        val orderedList = getOrderedList().toMutableList()
        orderedList.add(newCartItem)
        saveOrderedItem(orderedList)
    }

    fun addOrderedItemList(newCartItem: List<CartItems>) {
        val cartList = getOrderedList().toMutableList()
        cartList.addAll(newCartItem)
        saveOrderedItem(cartList)
    }

    // Delete an Address from the list
    fun deleteOrderedItem(cartItems: CartItems) {
        val orderedList = getOrderedList().toMutableList()
        orderedList.remove(cartItems)
        saveOrderedItem(orderedList)
    }

    fun clearOrderedItems() {
        val orderedList = getOrderedList().toMutableList()
        orderedList.clear()
        saveOrderedItem(orderedList)
    }

    // Get the list of Addresses
    fun getOrderedList(): List<CartItems> {
        val json = prefs.getString("OrderedList", null)
        return if (json != null) {
            val type = object : TypeToken<List<CartItems>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun getOrderedListAsLive(): List<CartItems>? {
        val json = prefs.getString("OrderedList", null)
        return if (json != null) {
            val type = object : TypeToken<List<CartItems>>() {}.type
            gson.fromJson(json, type)
        } else {
            null
        }
    }

    // Save the Address list to SharedPreferences
    private fun saveOrderedItem(cartList: List<CartItems>) {
        val json = gson.toJson(cartList)
        editor.putString("OrderedList", json)
        editor.apply()
    }


}
package com.example.flipkartclone.data.user

import android.content.Context
import android.content.SharedPreferences
import com.example.flipkartclone.domain.models.user.CartItems
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.Provides
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserCartItemsPref @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs: SharedPreferences = context.getSharedPreferences("CartDetails", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()
    private val gson = Gson()

    // Add an Address to the list
    fun addCartItem(newCartItem: CartItems) {
        val cartList = getCartList().toMutableList()
        cartList.add(newCartItem)
        saveCartItem(cartList)
    }

    // Delete an Address from the list
    fun deleteCartItem(cartItems: CartItems) {
        val cartList = getCartList().toMutableList()
        cartList.remove(cartItems)
        saveCartItem(cartList)
    }

    fun clearCartItems() {
        val cartList = getCartList().toMutableList()
        cartList.clear()
        saveCartItem(cartList)
    }

    // Get the list of Addresses
    fun getCartList(): List<CartItems> {
        val json = prefs.getString("CartList", null)
        return if (json != null) {
            val type = object : TypeToken<List<CartItems>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun getCartListAsLive(): List<CartItems>? {
        val json = prefs.getString("CartList", null)
        return if (json != null) {
            val type = object : TypeToken<List<CartItems>>() {}.type
            gson.fromJson(json, type)
        } else {
            null
        }
    }

    // Save the Address list to SharedPreferences
    private fun saveCartItem(cartList: List<CartItems>) {
        val json = gson.toJson(cartList)
        editor.putString("CartList", json)
        editor.apply()
    }


}
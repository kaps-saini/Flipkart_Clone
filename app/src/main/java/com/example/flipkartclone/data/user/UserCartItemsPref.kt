package com.example.flipkartclone.data.user

import android.content.Context
import android.content.SharedPreferences
import com.example.flipkartclone.domain.models.user.CartItems
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserCartItemsPref @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val prefs: SharedPreferences = context.getSharedPreferences("CartDetails", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()
    private val gson = Gson()

    // Add an item to the cart
    fun addCartItem(newCartItem: CartItems) {
        val cartList = getCartList().toMutableList()
        cartList.add(newCartItem)
        saveCartItem(cartList)
    }

    // Delete an item from the cart
    fun deleteCartItem(cartItem: CartItems) {
        val cartList = getCartList().toMutableList()
        cartList.remove(cartItem) // Ensure CartItems has equals() implemented
        saveCartItem(cartList)
    }

    // Clear all cart items
    fun clearCartItems() {
        editor.remove("CartList").apply()
    }

    // Get the list of cart items
    fun getCartList(): List<CartItems> {
        val json = prefs.getString("CartList", null) ?: return emptyList()
        val type = object : TypeToken<List<CartItems>>() {}.type
        return gson.fromJson(json, type)
    }

    // Save the cart list
    private fun saveCartItem(cartList: List<CartItems>) {
        val json = gson.toJson(cartList)
        editor.putString("CartList", json)
        editor.apply() // Use commit() if you need immediate saving
    }
}

package com.example.flipkartclone.data.user

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flipkartclone.domain.models.user.Address
import com.example.flipkartclone.domain.models.user.UserDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class UserDetailsPref @Inject constructor(
    @ActivityContext private val context:Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()
    private val gson = Gson()

    // Save User Details
    private fun saveUserDetails(userDetails: UserDetails) {
        val json = gson.toJson(userDetails)
        editor.putString("UserDetails", json)
        editor.apply()
    }

    // Get User Details
    fun getUserDetails(): UserDetails? {
        val json = prefs.getString("UserDetails", null)
        return if (json != null) {
            gson.fromJson(json, UserDetails::class.java)
        } else {
            null
        }
    }

    // Edit User Details
    fun editUserDetails(newUserDetails: UserDetails) {
        val existingUserDetails = getUserDetails()

        val updatedUserDetails = existingUserDetails?.apply {
            newUserDetails.firstName?.let { this.firstName = it }
            newUserDetails.lastName?.let { this.lastName = it }
            newUserDetails.userId?.let { this.userId = it }
            newUserDetails.phoneNumber?.let { this.phoneNumber = it }
            newUserDetails.gender?.let { this.gender = it }
            newUserDetails.address?.let {
                this.address = this.address?.apply {
                    it.name?.let { name = it }
                    it.phone?.let { phone = it }
                    it.pincode?.let { pincode = it }
                    it.state?.let { state = it }
                    it.city?.let { city = it }
                    it.houseNo?.let { houseNo = it }
                    it.area?.let { area = it }
                    it.type?.let { type = it }
                    it.location?.let { location = it }
                } ?: it
            }
        } ?: newUserDetails

        saveUserDetails(updatedUserDetails)
    }

    // Delete User Details
    fun deleteUserDetails() {
        editor.remove("UserDetails")
        editor.apply()
    }

    // Edit Specific Field (Example: Edit Username)
    fun editUserName(newUserFirstName: String,newUserLastName: String) {
        val userDetails = getUserDetails()
        userDetails?.firstName = newUserFirstName
        userDetails?.lastName = newUserLastName
        saveUserDetails(userDetails!!)
    }

    // Edit Specific Field (Example: Edit Phone Number)
    fun editPhoneNumber(newPhoneNumber: String) {
        val userDetails = getUserDetails()
        userDetails?.phoneNumber = newPhoneNumber
        saveUserDetails(userDetails!!)
    }

    // Check if User Exists by Phone Number
    fun doesUserExist(phoneNumber: String): Boolean {
        val userDetails = getUserDetails()
        return userDetails?.phoneNumber == phoneNumber
    }

    // Add an Address to the list
    fun addAddress(newAddress: Address) {
        val addressList = getAddressList().toMutableList()
        addressList.add(newAddress)
        saveAddressList(addressList)
    }

    // Delete an Address from the list
    fun deleteAddress(address: Address) {
        val addressList = getAddressList().toMutableList()
        addressList.remove(address)
        saveAddressList(addressList)
    }

    // Get the list of Addresses
    fun getAddressList(): List<Address> {
        val json = prefs.getString("AddressList", null)
        return if (json != null) {
            val type = object : TypeToken<List<Address>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun getAddressListAsLive(): List<Address>? {
        val json = prefs.getString("AddressList", null)
        return if (json != null) {
            val type = object : TypeToken<List<Address>>() {}.type
            gson.fromJson(json, type)
        } else {
            null
        }
    }


    // Save the Address list to SharedPreferences
    private fun saveAddressList(addressList: List<Address>) {
        val json = gson.toJson(addressList)
        editor.putString("AddressList", json)
        editor.apply()
    }
}
package com.example.flipkartclone.data.cache

import androidx.room.TypeConverter
import com.example.flipkartclone.domain.models.Highlight
import com.example.flipkartclone.domain.models.Item
import com.example.flipkartclone.domain.models.Pricing
import com.example.flipkartclone.domain.models.Ratings
import com.example.flipkartclone.domain.models.Review
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromListToString(images: List<String>?): String {
        return gson.toJson(images)
    }

    @TypeConverter
    fun fromStringToList(data: String?): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fromHighlightList(value: List<Highlight>): String = gson.toJson(value)

    @TypeConverter
    fun toHighlightList(value: String): List<Highlight> {
        val type = object : TypeToken<List<Highlight>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromReviewList(value: List<Review>?): String = gson.toJson(value)

    @TypeConverter
    fun toReviewList(value: String): List<Review>? {
        val type = object : TypeToken<List<Review>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromItem(value: Item): String = gson.toJson(value)

    @TypeConverter
    fun toItem(value: String): Item = gson.fromJson(value, Item::class.java)

    @TypeConverter
    fun fromPricing(value: Pricing): String = gson.toJson(value)

    @TypeConverter
    fun toPricing(value: String): Pricing = gson.fromJson(value, Pricing::class.java)

    @TypeConverter
    fun fromRatings(value: Ratings): String = gson.toJson(value)

    @TypeConverter
    fun toRatings(value: String): Ratings = gson.fromJson(value, Ratings::class.java)
}

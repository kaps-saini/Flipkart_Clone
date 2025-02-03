package com.example.flipkartclone.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.flipkartclone.domain.models.BrandsForYou
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
import com.example.flipkartclone.domain.models.Sponsor

@TypeConverters(Converters::class)
@Database(entities = [ItemModelItem::class,ItemDataModel::class,BrandsForYou::class,Sponsor::class], version = 2, exportSchema = false)
abstract class AppDataBase:RoomDatabase() {
    abstract fun itemDao():ItemDao
    abstract fun droppedItemDao():DroppedItemDao
    abstract fun brandsForYouDao():BrandsForYouDao
    abstract fun sponsorsDao():SponsorsDao
}
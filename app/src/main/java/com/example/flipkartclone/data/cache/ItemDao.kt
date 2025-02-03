package com.example.flipkartclone.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flipkartclone.domain.models.ItemModelItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM item")
    fun getAllItems(): Flow<List<ItemModelItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ItemModelItem>)

    @Delete
    suspend fun delete(items: ItemModelItem)

    @Query("DELETE FROM item")
    suspend fun clearAll()
}
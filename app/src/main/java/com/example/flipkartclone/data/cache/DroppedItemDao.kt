package com.example.flipkartclone.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flipkartclone.domain.models.ItemDataModel
import com.example.flipkartclone.domain.models.ItemModelItem
import kotlinx.coroutines.flow.Flow

@Dao
interface DroppedItemDao {

    @Query("SELECT * FROM droppedItem")
    fun getAllDroppedItems(): Flow<List<ItemDataModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items:List<ItemDataModel>)

    @Query("DELETE FROM droppedItem")
    suspend fun clearAll()
}
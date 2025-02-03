package com.example.flipkartclone.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flipkartclone.domain.models.BrandsForYou
import kotlinx.coroutines.flow.Flow

@Dao
interface BrandsForYouDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(brandsList:List<BrandsForYou>)

    @Query("SELECT * FROM brandsForYou")
    fun getAllBrandsList(): Flow<List<BrandsForYou>>

    @Query("DELETE FROM brandsForYou")
    suspend fun clearAll()

}
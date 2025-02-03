package com.example.flipkartclone.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flipkartclone.domain.models.Sponsor
import kotlinx.coroutines.flow.Flow

@Dao
interface SponsorsDao {

    @Query("SELECT * FROM sponsor_table")
    fun getAllSponsors(): Flow<List<Sponsor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Sponsor>)

    @Delete
    suspend fun delete(items: Sponsor)

    @Query("DELETE FROM sponsor_table")
    suspend fun clearAll()
}
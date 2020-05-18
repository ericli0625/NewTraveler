package com.example.eric.newtraveler.storage.dao

import androidx.room.*
import com.example.eric.newtraveler.network.response.AttractionInfo
import io.reactivex.Observable

@Dao
interface AttractionInfoDao {

    @Query("SELECT * FROM attractions")
    fun getAllAttractions(): Observable<List<AttractionInfo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attraction: AttractionInfo)

    @Delete
    suspend fun delete(attraction: AttractionInfo)
}
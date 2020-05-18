package com.example.eric.newtraveler.storage.dao

import androidx.room.*
import com.example.eric.newtraveler.network.response.AttractionDetail
import io.reactivex.Observable

@Dao
interface AttractionDetailDao {

    @Query("SELECT * FROM attractions")
    fun getAllAttractions(): Observable<List<AttractionDetail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attraction: AttractionDetail)

    @Delete
    suspend fun delete(attraction: AttractionDetail)
}
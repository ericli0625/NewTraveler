package com.example.eric.newtraveler.ui.attraction.detail

import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.storage.BaseRepository

class AttractionDetailRepository : BaseRepository() {

    suspend fun addFavorite(attraction: AttractionInfo) {
        attractionInfoDao.insert(attraction)
    }

    suspend fun deleteFavorite(attraction: AttractionInfo) {
        attractionInfoDao.delete(attraction)
    }
}
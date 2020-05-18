package com.example.eric.newtraveler.ui.attraction.detail

import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.storage.BaseRepository

class AttractionDetailRepository : BaseRepository() {

    suspend fun addFavorite(attraction: AttractionDetail) {
        attractionDetailDao.insert(attraction)
    }

    suspend fun deleteFavorite(attraction: AttractionDetail) {
        attractionDetailDao.delete(attraction)
    }
}
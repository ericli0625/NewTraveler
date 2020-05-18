package com.example.eric.newtraveler.ui.attraction.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class AttractionDetailViewModel(
        private val repository: AttractionDetailRepository
) : BaseViewModel(repository) {

    private val _showOrHideFavorIcon by lazy {
        MutableLiveData<Event<Boolean>>()
    }
    val showOrHideFavorIcon: LiveData<Event<Boolean>> by lazy {
        _showOrHideFavorIcon
    }

    private val disposables = CompositeDisposable()

    fun addFavorite(attraction: AttractionDetail) {
        viewModelScope.launch {
            repository.addFavorite(attraction)
        }
    }

    fun deleteFavorite(attraction: AttractionDetail) {
        viewModelScope.launch {
            repository.deleteFavorite(attraction)
        }
    }

    fun showOrHideFavorIcon(name: String) {
        disposables.add(
                repository.getAllAttractions()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val attraction = it.firstOrNull { it.name == name }
                                    ?: AttractionDetail.defaultInstance
                            _showOrHideFavorIcon.value = Event(attraction.name == name)
                        }
        )
    }

    override fun onCleared() {
        disposables.dispose()
    }
}
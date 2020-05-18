package com.example.eric.newtraveler.ui.attraction.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.network.response.AttractionInfo
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

    private val _showSnackBarEvent by lazy {
        MutableLiveData<Event<Int>>()
    }
    val showSnackBarEvent: LiveData<Event<Int>> by lazy {
        _showSnackBarEvent
    }

    private val disposables = CompositeDisposable()

    fun addFavorite(attraction: AttractionInfo) {
        viewModelScope.launch {
            repository.addFavorite(attraction)
            _showSnackBarEvent.value = Event(R.string.snack_bar_add_msg)
        }
    }

    fun deleteFavorite(attraction: AttractionInfo) {
        viewModelScope.launch {
            repository.deleteFavorite(attraction)
            _showSnackBarEvent.value = Event(R.string.snack_bar_delete_msg)
        }
    }

    fun showOrHideFavorIcon(name: String) {
        disposables.add(
                repository.getAllAttractions()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            val attraction = it.firstOrNull { it.name == name }
                                    ?: AttractionInfo.defaultInstance
                            _showOrHideFavorIcon.value = Event(attraction.name == name)
                        }
        )
    }

    override fun onCleared() {
        disposables.dispose()
    }
}
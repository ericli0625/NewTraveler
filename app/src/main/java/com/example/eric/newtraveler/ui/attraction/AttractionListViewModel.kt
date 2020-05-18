package com.example.eric.newtraveler.ui.attraction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.ui.base.BaseViewModel
import com.kkday.scm.util.wrapper.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AttractionListViewModel(private val repository: AttractionListRepository) :
        BaseViewModel(repository) {

    private val _showAttractionListEvent by lazy {
        MutableLiveData<Event<List<AttractionInfo>>>()
    }
    val showAttractionListEvent: LiveData<Event<List<AttractionInfo>>> by lazy {
        _showAttractionListEvent
    }

    private val disposables = CompositeDisposable()

    fun queryAttractionList(countyName: String, cityName: String) {
        disposables.add(
                repository.getAttractionInfoByPlace(countyName, cityName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            _showAttractionListEvent.value = Event(it)
                        }
        )
    }

    override fun onCleared() {
        disposables.dispose()
    }
}
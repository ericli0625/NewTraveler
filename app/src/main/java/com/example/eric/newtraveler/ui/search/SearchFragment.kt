package com.example.eric.newtraveler.ui.search

import android.os.Bundle
import androidx.lifecycle.observe
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.ui.MainActivity
import com.example.eric.newtraveler.ui.OnSearchDataReceivedListener
import com.example.eric.newtraveler.ui.attraction.AttractionListAdapter
import com.example.eric.newtraveler.ui.attraction.detail.AttractionDetailActivity
import com.example.eric.newtraveler.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<SearchViewModel>(), OnSearchDataReceivedListener {

    override val layoutRes: Int = R.layout.fragment_search
    override val viewModel: SearchViewModel by viewModel()

    private val attractionListAdapter by lazy { AttractionListAdapter(::onClickItemListener) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLayout()
//        viewModel.fetchAndActivateRemoteConfig()
        (activity as MainActivity).setSearchDataListener(this)
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        subscribeToShowAttractionList()
    }

    private fun initLayout() {
        recycler_view.adapter = attractionListAdapter
    }

    private fun onClickItemListener(attraction: AttractionInfo) {
        AttractionDetailActivity.launch(requireContext(), attraction)
    }

    /***** Subscribe methods implementation *****/

    private fun subscribeToShowAttractionList() {
        viewModel.showAttractionListEvent.observe(this) {
            attractionListAdapter.updateData(it.peekContent())
        }
    }

    override fun onDataReceived(name: String) {
        viewModel.searchAttraction(name)
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
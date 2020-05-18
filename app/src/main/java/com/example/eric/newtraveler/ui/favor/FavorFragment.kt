package com.example.eric.newtraveler.ui.favor

import android.os.Bundle
import androidx.lifecycle.observe
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.ui.attraction.AttractionListAdapter
import com.example.eric.newtraveler.ui.attraction.detail.AttractionDetailActivity
import com.example.eric.newtraveler.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_attraction_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavorFragment : BaseFragment<FavorViewModel>() {

    override val layoutRes: Int = R.layout.fragment_favor
    override val viewModel: FavorViewModel by viewModel()

    private val attractionListAdapter by lazy { AttractionListAdapter(::onClickItemListener) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLayout()
        viewModel.getAllAttractions()
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        subscribeToShowAttractionList()
    }

    private fun initLayout() {
        recycler_view.adapter = attractionListAdapter
    }

    private fun onClickItemListener(attraction: AttractionDetail) {
        AttractionDetailActivity.launch(requireContext(), attraction)
    }

    /***** Subscribe methods implementation *****/

    private fun subscribeToShowAttractionList() {
        viewModel.showAttractionListEvent.observe(this) {
            attractionListAdapter.updateData(it.peekContent())
        }
    }

    companion object {
        fun newInstance() = FavorFragment()
    }
}
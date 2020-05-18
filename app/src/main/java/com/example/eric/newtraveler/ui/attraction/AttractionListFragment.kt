package com.example.eric.newtraveler.ui.attraction

import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.network.response.AttractionInfo
import com.example.eric.newtraveler.ui.attraction.detail.AttractionDetailActivity
import com.example.eric.newtraveler.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_attraction_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AttractionListFragment : BaseFragment<AttractionListViewModel>() {

    override val layoutRes: Int = R.layout.fragment_attraction_list
    override val viewModel: AttractionListViewModel by viewModel()

    private val attractionListAdapter by lazy { AttractionListAdapter(::onClickItemListener) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLayout()
        requireActivity().onBackPressedDispatcher.addCallback(this) { backFragment() }
        viewModel.queryAttractionList(getCountyName(), getCityName())
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        subscribeToShowAttractionList()
    }

    private fun initLayout() {
        recycler_view.adapter = attractionListAdapter
        image_arrow.setOnClickListener { backFragment() }
    }

    private fun backFragment() {
        activity?.supportFragmentManager?.commit {
            remove(this@AttractionListFragment)
        }
    }

    private fun onClickItemListener(attraction: AttractionInfo) {
        AttractionDetailActivity.launch(requireContext(), attraction)
    }

    private fun getCountyName(): String = arguments?.getString(COUNTY_NAME) ?: ""

    private fun getCityName(): String = arguments?.getString(CITY_NAME) ?: ""

    /***** Subscribe methods implementation *****/

    private fun subscribeToShowAttractionList() {
        viewModel.showAttractionListEvent.observe(this) {
            attractionListAdapter.updateData(it.peekContent())
        }
    }

    companion object {
        private const val COUNTY_NAME = "COUNTY_NAME"
        private const val CITY_NAME = "CITY_NAME"

        fun newInstance() = AttractionListFragment()
    }
}
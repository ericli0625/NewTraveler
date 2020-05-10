package com.example.eric.newtraveler.ui.home

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.observe
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.ui.attraction.AttractionListFragment
import com.example.eric.newtraveler.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel>() {

    override val layoutRes: Int = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModel()

    private val homeViewInfoAdapter by lazy {
        HomeViewInfoAdapter(::onClickItemListener)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLayout()
        viewModel.onActivityCreated()
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        subscribeToShowCountyList()
        subscribeToShowCityList()
        subscribeToShowAttractionList()
    }

    private fun initLayout() {
        recycler_view.adapter = homeViewInfoAdapter
    }

    private fun onClickItemListener(countyName: String) {
        viewModel.showCardItems(countyName)
    }

    /***** Subscribe methods implementation *****/

    private fun subscribeToShowCountyList() {
        viewModel.showCountyListEvent.observe(this) {
            homeViewInfoAdapter.updateData(it.peekContent())
        }
    }

    private fun subscribeToShowCityList() {
        viewModel.showCityListEvent.observe(this) {
            homeViewInfoAdapter.updateData(it.peekContent())
        }
    }

    private fun subscribeToShowAttractionList() {
        viewModel.showAttractionListEvent.observe(this) {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.add(
                            R.id.layout_home,
                            AttractionListFragment.newInstance().apply {
                                val (countyName, cityName) = it.peekContent()
                                arguments = bundleOf(
                                        COUNTY_NAME to countyName,
                                        CITY_NAME to cityName
                                )
                            }
                    )
                    ?.commit()
        }
    }

    companion object {
        private const val COUNTY_NAME = "COUNTY_NAME"
        private const val CITY_NAME = "CITY_NAME"

        fun newInstance() = HomeFragment()
    }
}
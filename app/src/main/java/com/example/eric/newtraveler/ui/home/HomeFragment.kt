package com.example.eric.newtraveler.ui.home

import android.os.Bundle
import androidx.lifecycle.observe
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel>() {

    override val layoutRes: Int = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModel()

    private val homeViewInfoAdapter by lazy {
        HomeViewInfoAdapter(::onCountyClickListener)
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
    }

    private fun initLayout() {
        recycler_view.adapter = homeViewInfoAdapter
    }

    private fun onCountyClickListener(countyName: String) {
        viewModel.getCityList(countyName)
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

    companion object {
        fun newInstance() = HomeFragment()
    }
}
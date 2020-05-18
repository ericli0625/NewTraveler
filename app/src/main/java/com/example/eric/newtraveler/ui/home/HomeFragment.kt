package com.example.eric.newtraveler.ui.home

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.lifecycle.observe
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.ui.attraction.AttractionListFragment
import com.example.eric.newtraveler.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel>() {

    override val layoutRes: Int = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.onActivityCreated()
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        subscribeToQueryLocationList()
        subscribeToShowCountyList()
    }

    /***** Subscribe methods implementation *****/

    private fun subscribeToQueryLocationList() {
        viewModel.queryLocationList.observe(this) {
            viewModel.showCountyList()
        }
    }

    private fun subscribeToShowCountyList() {
        viewModel.showCountyListEvent.observe(this) {
            val adapter = ExpandableListViewAdapter(
                    requireContext(), it.peekContent().first, it.peekContent().second
            )

            with(list_view) {
                setAdapter(adapter)
                setOnChildClickListener { _, _, groupPosition, childPosition, _ ->
                    val countyName = adapter.getGroup(groupPosition).toString()
                    val cityName = adapter.getChild(groupPosition, childPosition).toString()
                    showAttractionList(countyName, cityName)
                    false
                }
            }
        }
    }

    private fun showAttractionList(countyName: String, cityName: String) {
        activity?.supportFragmentManager?.commit {
            replace(
                    R.id.layout_home,
                    AttractionListFragment.newInstance().apply {
                        arguments = bundleOf(
                                COUNTY_NAME to countyName,
                                CITY_NAME to cityName
                        )
                    }
            )
        }
    }

    companion object {
        private const val COUNTY_NAME = "COUNTY_NAME"
        private const val CITY_NAME = "CITY_NAME"

        fun newInstance() = HomeFragment()
    }
}
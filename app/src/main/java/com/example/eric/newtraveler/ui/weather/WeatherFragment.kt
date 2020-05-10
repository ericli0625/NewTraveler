package com.example.eric.newtraveler.ui.weather

import android.os.Bundle
import androidx.lifecycle.observe
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.ui.base.BaseFragment
import com.example.eric.newtraveler.ui.weather.detail.WeatherDetailActivity
import kotlinx.android.synthetic.main.fragment_weather.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : BaseFragment<WeatherViewModel>() {

    override val layoutRes: Int = R.layout.fragment_weather
    override val viewModel: WeatherViewModel by viewModel()

    private val weatherAdapter by lazy { WeatherAdapter(::onCountyClickListener) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initLayout()
        viewModel.onActivityCreated()
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        subscribeToShowCountyList()
    }

    private fun initLayout() {
        recycler_view.adapter = weatherAdapter
    }

    private fun onCountyClickListener(countyName: String) {
        WeatherDetailActivity.launch(requireContext(), countyName)
    }

    /***** Subscribe methods implementation *****/

    private fun subscribeToShowCountyList() {
        viewModel.showCountyListEvent.observe(this) {
            weatherAdapter.updateData(it.peekContent())
        }
    }

    companion object {
        fun newInstance() = WeatherFragment()
    }
}
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

    private fun initLayout() {
        recycler_view.adapter = weatherAdapter
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        subscribeToShowCountyList()
        subscribeToShowWeather()
    }

    private fun onCountyClickListener(countyName: String) {
        viewModel.queryWeatherForecast(countyName)
    }

    /***** Subscribe methods implementation *****/

    private fun subscribeToShowCountyList() {
        viewModel.showCountyListEvent.observe(this) {
            weatherAdapter.updateData(it.peekContent())
        }
    }

    private fun subscribeToShowWeather() {
        viewModel.showWeatherEvent.observe(this) {
            WeatherDetailActivity.launch(requireContext(), it.peekContent())
        }
    }
}
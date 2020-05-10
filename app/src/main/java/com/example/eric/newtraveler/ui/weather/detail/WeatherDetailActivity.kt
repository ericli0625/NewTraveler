package com.example.eric.newtraveler.ui.weather.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.observe
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.model.WeatherViewInfo
import com.example.eric.newtraveler.network.response.WeatherElement
import com.example.eric.newtraveler.ui.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_weather_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherDetailActivity : BaseActivity<WeatherDetailViewModel>() {

    override val layoutRes: Int = R.layout.fragment_weather_detail
    override val viewModel: WeatherDetailViewModel by viewModel()

    private val weatherDetailAdapter by lazy { WeatherDetailAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationName = intent.extras?.getString("locationName") ?: ""
        initLayout(locationName)
        viewModel.queryWeatherForecast(locationName)
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        subscribeToShowWeather()
    }

    private fun initLayout(locationName: String) {
        recyclerView_weather_forecast_detail.adapter = weatherDetailAdapter

        with(topAppBar) {
            title = locationName
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun weatherViewInfoConverter(
            weatherElementArray: List<WeatherElement>
    ): WeatherViewInfo {
        val first = weatherElementArray.map { it.time[0] }
        val second = weatherElementArray.map { it.time[1] }
        val third = weatherElementArray.map { it.time[2] }
        return WeatherViewInfo(listOf(first, second, third))
    }

    /***** Subscribe methods implementation *****/

    private fun subscribeToShowWeather() {
        viewModel.showWeatherEvent.observe(this) {
            val viewInfo = weatherViewInfoConverter(it.peekContent())
            weatherDetailAdapter.updateData(viewInfo)
        }
    }

    companion object {
        fun launch(context: Context, locationName: String) {
            val intent = Intent(context, WeatherDetailActivity::class.java).apply {
                putExtra("locationName", locationName)
            }
            context.startActivity(intent)
        }
    }
}
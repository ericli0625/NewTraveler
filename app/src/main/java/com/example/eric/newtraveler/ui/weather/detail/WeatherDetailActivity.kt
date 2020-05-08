package com.example.eric.newtraveler.ui.weather.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.model.WeatherViewInfo
import com.example.eric.newtraveler.network.response.Weather.WeatherElement
import com.example.eric.newtraveler.ui.base.BaseActivity
import kotlinx.android.synthetic.main.fragment_weather_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class WeatherDetailActivity : BaseActivity<WeatherDetailViewModel>() {

    override val layoutRes: Int = R.layout.fragment_weather_detail
    override val viewModel: WeatherDetailViewModel by viewModel()

    private val weatherDetailAdapter by lazy { WeatherDetailAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLayout(intent.extras)
    }

    private fun initLayout(bundle: Bundle?) {
        recyclerView_weather_forecast_detail.adapter = weatherDetailAdapter

        val weatherElementArray: ArrayList<WeatherElement> =
                bundle?.getParcelableArrayList("weatherElementArray") ?: arrayListOf()
        val locationName = bundle?.getString("locationName") ?: ""

        with(topAppBar) {
            title = locationName
            setNavigationOnClickListener { onBackPressed() }
        }

        setupRecycleView(weatherElementArray)
    }

    private fun setupRecycleView(weatherElementArray: ArrayList<WeatherElement>) {
        val viewInfo = weatherViewInfoConverter(weatherElementArray)
        weatherDetailAdapter.updateData(viewInfo)
    }

    private fun weatherViewInfoConverter(
            weatherElementArray: ArrayList<WeatherElement>
    ): WeatherViewInfo {
        val first = weatherElementArray.map { it.time[0] }
        val second = weatherElementArray.map { it.time[1] }
        val third = weatherElementArray.map { it.time[2] }
        return WeatherViewInfo(listOf(first, second, third))
    }

    companion object {
        fun launch(
                context: Context,
                peekContent: Pair<ArrayList<WeatherElement>, String>
        ) {
            val bundle = Bundle().apply {
                putParcelableArrayList("weatherElementArray", peekContent.first)
                putString("locationName", peekContent.second)
            }
            val intent = Intent(context, WeatherDetailActivity::class.java).apply {
                putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }
}
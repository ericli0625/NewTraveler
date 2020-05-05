package com.example.eric.newtraveler.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.adapters.WeatherDetailAdapter
import com.example.eric.newtraveler.model.WeatherViewInfo
import com.example.eric.newtraveler.network.response.Weather.WeatherElement
import kotlinx.android.synthetic.main.activity_weather_detail.*
import java.util.*

class WeatherDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_detail)
        loadInitCommonView(intent.extras)
    }

    private fun loadInitCommonView(bundle: Bundle?) {
        val weatherElementArray: ArrayList<WeatherElement> =
                bundle?.getParcelableArrayList("weatherElementArray") ?: arrayListOf()
        val locationName = bundle?.getString("locationName") ?: ""

        setupRecycleView(weatherElementArray)

        with(topAppBar) {
            title = locationName
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    private fun setupRecycleView(weatherElementArray: ArrayList<WeatherElement>) {
        val viewInfo = weatherViewInfoConverter(weatherElementArray)
        val weatherDetailAdapter = WeatherDetailAdapter(viewInfo)

        with(recyclerView_weather_forecast_detail) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = weatherDetailAdapter
        }
        weatherDetailAdapter.notifyDataSetChanged()
    }

    private fun weatherViewInfoConverter(
            weatherElementArray: ArrayList<WeatherElement>
    ): WeatherViewInfo {
        val first = weatherElementArray.map { it.time[0] }
        val second = weatherElementArray.map { it.time[1] }
        val third = weatherElementArray.map { it.time[2] }
        return WeatherViewInfo(listOf(first, second, third))
    }
}
package com.example.eric.newtraveler.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.adapters.WeatherDetailAdapter
import com.example.eric.newtraveler.model.WeatherViewInfo
import com.example.eric.newtraveler.network.response.Weather.WeatherElement
import kotlinx.android.synthetic.main.fragment_weather_detail.*
import java.util.*

class WeatherDetailFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun loadInitCommonView(bundle: Bundle?) {
        val weatherElementArray: ArrayList<WeatherElement> =
                bundle?.getParcelableArrayList("weatherElementArray") ?: arrayListOf()
        val locationName = bundle?.getString("locationName") ?: ""

        setupRecycleView(weatherElementArray)

        with(topAppBar) {
            title = locationName
            setNavigationOnClickListener {

            }
        }
    }

    private fun setupRecycleView(weatherElementArray: ArrayList<WeatherElement>) {
        val viewInfo = weatherViewInfoConverter(weatherElementArray)
        val weatherDetailAdapter = WeatherDetailAdapter(viewInfo)

        with(recyclerView_weather_forecast_detail) {
            setHasFixedSize(true)
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
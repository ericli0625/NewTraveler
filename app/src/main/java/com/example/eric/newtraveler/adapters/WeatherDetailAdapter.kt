package com.example.eric.newtraveler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.model.WeatherViewInfo
import com.example.eric.newtraveler.network.response.Weather
import kotlinx.android.synthetic.main.weather_card_view.view.*

class WeatherDetailAdapter(
        private val weatherViewInfo: WeatherViewInfo
) : RecyclerView.Adapter<WeatherDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(weatherViewInfo.weatherParameterList[position])

    override fun getItemCount() = weatherViewInfo.weatherParameterList.size

    class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.weather_card_view, parent, false)) {

        fun bind(data: List<Weather.Time?>) {
            with(itemView) {
                forecast_time_text.text =
                        String.format(
                                resources.getString(
                                        R.string.weather_date_title,
                                        data.firstOrNull()?.startTime,
                                        data.firstOrNull()?.endTime
                                )
                        )
                condition_text.text =
                        String.format(
                                resources.getString(
                                        R.string.weather_condition,
                                        data[0]?.parameter?.parameterName
                                )
                        )
                comfortable_text.text =
                        String.format(
                                resources.getString(
                                        R.string.weather_comfortable,
                                        data[3]?.parameter?.parameterName
                                )
                        )
                rain_opportunity_text.text =
                        String.format(
                                resources.getString(
                                        R.string.weather_opportunity,
                                        data[1]?.parameter?.parameterName
                                )
                        )
                min_temporary_text.text =
                        String.format(
                                resources.getString(
                                        R.string.weather_min_temporary,
                                        data[2]?.parameter?.parameterName
                                )
                        )
                max_temporary_text.text =
                        String.format(
                                resources.getString(
                                        R.string.weather_max_temporary,
                                        data[4]?.parameter?.parameterName
                                )
                        )
            }
        }
    }
}
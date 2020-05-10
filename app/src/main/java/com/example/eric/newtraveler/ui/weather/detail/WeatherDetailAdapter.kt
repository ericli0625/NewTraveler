package com.example.eric.newtraveler.ui.weather.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.model.WeatherViewInfo
import com.example.eric.newtraveler.network.response.Time
import kotlinx.android.synthetic.main.weather_card_view.view.*

class WeatherDetailAdapter : RecyclerView.Adapter<WeatherDetailAdapter.ViewHolder>() {

    private var weatherViewInfo = WeatherViewInfo.defaultInstance

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(
                    parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
            holder.bind(weatherViewInfo.weatherParameterList[position])

    override fun getItemCount() = weatherViewInfo.weatherParameterList.size

    fun updateData(weatherViewInfo: WeatherViewInfo) {
        this.weatherViewInfo = weatherViewInfo
        notifyDataSetChanged()
    }

    class ViewHolder(parent: ViewGroup) :
            RecyclerView.ViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.weather_card_view, parent, false)) {

        fun bind(data: List<Time>) {
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
                                        data[0].parameter?.name
                                )
                        )
                comfortable_text.text =
                        String.format(
                                resources.getString(
                                        R.string.weather_comfortable,
                                        data[3].parameter?.name
                                )
                        )
                rain_opportunity_text.text =
                        String.format(
                                resources.getString(
                                        R.string.weather_opportunity,
                                        data[1].parameter?.name
                                )
                        )
                min_temporary_text.text =
                        String.format(
                                resources.getString(
                                        R.string.weather_min_temporary,
                                        data[2].parameter?.name
                                )
                        )
                max_temporary_text.text =
                        String.format(
                                resources.getString(
                                        R.string.weather_max_temporary,
                                        data[4].parameter?.name
                                )
                        )
            }
        }
    }
}
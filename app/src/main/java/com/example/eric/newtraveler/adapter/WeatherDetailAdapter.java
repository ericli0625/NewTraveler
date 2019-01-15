package com.example.eric.newtraveler.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eric.newtraveler.R;
import com.example.eric.newtraveler.parcelable.Weather;

import java.util.ArrayList;

public class WeatherDetailAdapter extends RecyclerView.Adapter<WeatherDetailAdapter.ViewHolder> {

    private final ArrayList<Weather.WeatherElement> mWeatherElementArray;
    private String[] mTitleArray;

    public WeatherDetailAdapter(ArrayList<Weather.WeatherElement> weatherElementArray, String[] stringArray) {
        mWeatherElementArray = weatherElementArray;
        mTitleArray = stringArray;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTitleTextView;
        protected TextView mTimeTextView12;
        protected TextView mTimeTextView24;
        protected TextView mTimeTextView36;

        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
            mTitleTextView = (TextView) v.findViewById(R.id.recycle_view_item_title);
            mTimeTextView12 = (TextView) v.findViewById(R.id.recycle_view_item_12hr);
            mTimeTextView24 = (TextView) v.findViewById(R.id.recycle_view_time_24hr);
            mTimeTextView36 = (TextView) v.findViewById(R.id.recycle_view_time_36hr);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_item_view,
                viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Weather.WeatherElement weatherElement = mWeatherElementArray.get(position);
        ArrayList<Weather.Time> timeArray = weatherElement.getTime();

        viewHolder.mTitleTextView.setText(mTitleArray[position]);

        for (int j = 0; j < timeArray.size(); j++) {
            Weather.Parameter parameter = timeArray.get(j).getParameter();
            String parameterName = parameter.getParameterName();
            switch (j) {
                case 0:
                    viewHolder.mTimeTextView12.setText(parameterName);
                    break;
                case 1:
                    viewHolder.mTimeTextView24.setText(parameterName);
                    break;
                case 2:
                    viewHolder.mTimeTextView36.setText(parameterName);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mWeatherElementArray == null ? 0 : mWeatherElementArray.size();
    }

}

package com.example.eric.newtraveler.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.eric.newtraveler.MainActivity;
import com.example.eric.newtraveler.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WeatherDetailAdapter extends RecyclerView.Adapter<WeatherDetailAdapter.ViewHolder> {

    private JSONArray mJsonArray;

    private ArrayList<ArrayList<String>> mWeatherArrayList = new ArrayList<>();

    public WeatherDetailAdapter() {

    }

    public void setJsonArray(String result) {
        try {
            mJsonArray = new JSONArray(result);

            for (int i = 0; i < mJsonArray.length(); i++) {
                Log.i(MainActivity.TAG,
                        "MainActivity, showWeatherForecastResult init i = " + i);
                try {
                    JSONObject weatherElement = (JSONObject) mJsonArray.get(i);
                    JSONArray timeArray = weatherElement.getJSONArray("time");

                    for (int j = 0; j < timeArray.length(); j++) {
                        String startTime = timeArray.getJSONObject(j).getString("startTime");
                        String endTime = timeArray.getJSONObject(j).getString("endTime");
                        JSONObject parameter = timeArray.getJSONObject(j).getJSONObject(
                                "parameter");
                        Log.w(MainActivity.TAG,
                                "MainActivity, showWeatherForecastResult j = " + j + " startTime "
                                        + startTime);
                        Log.w(MainActivity.TAG,
                                "MainActivity, showWeatherForecastResult j = " + j + " endTime "
                                        + startTime);
                        Log.w(MainActivity.TAG,
                                "MainActivity, showWeatherForecastResult j = " + j + " parameter "
                                        + parameter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            Log.e(MainActivity.TAG,
                    "WeatherDetailAdapter, parser weather info failed, JSONException");
        }
    }

    protected JSONArray getJsonArray() {
        return mJsonArray;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTimeTextView1;
        protected TextView mTimeTextView2;
        protected TextView mTimeTextView3;

        // each data item is just a string in this case
        public ViewHolder(View v) {
            super(v);
            mTimeTextView1 = (TextView) v.findViewById(R.id.recycle_view_item_8hr);
            mTimeTextView2 = (TextView) v.findViewById(R.id.recycle_view_time_16hr);
            mTimeTextView3 = (TextView) v.findViewById(R.id.recycle_view_time_24hr);
        }
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wether_item_view,
                viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String string = null;
        try {
            JSONObject weatherElement = (JSONObject) mJsonArray.get(position);
            JSONArray timeArray = weatherElement.getJSONArray("time");

            for (int j = 0; j < timeArray.length(); j++) {
                JSONObject parameter = timeArray.getJSONObject(j).getJSONObject("parameter");
                String parameterName = parameter.getString("parameterName");
                switch (j) {
                    case 0:
                        viewHolder.mTimeTextView1.setText(parameterName);
                        break;
                    case 1:
                        viewHolder.mTimeTextView2.setText(parameterName);
                        break;
                    case 2:
                        viewHolder.mTimeTextView3.setText(parameterName);
                        break;
                }
            }

        } catch (JSONException e) {
            Log.e(MainActivity.TAG, "WeatherDetailAdapter onBindViewHolder, JSONException");
        }
    }

    @Override
    public int getItemCount() {
        return mJsonArray == null ? 0 : mJsonArray.length();
    }

}

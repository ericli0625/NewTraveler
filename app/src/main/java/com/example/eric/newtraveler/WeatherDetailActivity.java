package com.example.eric.newtraveler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eric.newtraveler.adapter.WeatherDetailAdapter;
import com.example.eric.newtraveler.parcelable.Weather;

import java.util.ArrayList;

public class WeatherDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        Log.v(MainActivity.TAG, "WeatherDetailActivity, onCreate ");

        Bundle bundle = getIntent().getExtras();
        loadInitCommonView(bundle);
    }

    public void loadInitCommonView(Bundle bundle) {

        ImageButton returnIcon = (ImageButton) findViewById(R.id.return_icon);
        returnIcon.setOnClickListener(mButtonListener);

        RecyclerView mRecyclerView = getRecycleView(R.id.recyclerView_weather_forecast_detail);

        if (bundle != null) {
            ArrayList<Weather.WeatherElement> weatherElementArray = bundle.getParcelableArrayList("weatherElementArray");
            String locationName = bundle.getString("locationName");
            setWeatherForecastTime(weatherElementArray, locationName);
            WeatherDetailAdapter adapter = new WeatherDetailAdapter(weatherElementArray,
                    getResources().getStringArray(R.array.weather_title_array));
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

    }

    private void setWeatherForecastTime(ArrayList<Weather.WeatherElement> weatherElementArray, String locationName) {

        TextView locationTextView = (TextView) findViewById(R.id.location_name);
        locationTextView.setText(locationName);

        ArrayList<Weather.Time> timeArray = weatherElementArray.get(0).getTime();
        for (int j = 0; j < timeArray.size(); j++) {
            String startTime = timeArray.get(j).getStartTime();
            startTime = startTime.substring(5, startTime.length() - 3);
            String endTime = timeArray.get(j).getEndTime();
            endTime = endTime.substring(5, endTime.length() - 3);
            switch (j) {
                case 0:
                    TextView timeTextView12 = (TextView) findViewById(R.id.time_item_12hr);
                    timeTextView12.setText(startTime + "\n~\n" + endTime);
                    break;
                case 1:
                    TextView timeTextView24 = (TextView) findViewById(R.id.time_item_24hr);
                    timeTextView24.setText(startTime + "\n~\n" + endTime);
                    break;
                case 2:
                    TextView timeTextView36 = (TextView) findViewById(R.id.time_item_36hr);
                    timeTextView36.setText(startTime + "\n~\n" + endTime);
                    break;
            }
        }
    }

    private RecyclerView getRecycleView(int recyclerViewId) {
        // use a recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(recyclerViewId);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        return recyclerView;
    }

    public View.OnClickListener mButtonListener = v -> {
        setContentView(R.layout.activity_main);
        onBackPressed();
    };

}

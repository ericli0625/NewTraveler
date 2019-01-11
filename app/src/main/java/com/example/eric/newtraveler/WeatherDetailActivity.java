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

import com.example.eric.newtraveler.adapter.RecyclerItemTouchListener;
import com.example.eric.newtraveler.adapter.WeatherDetailAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private WeatherDetailAdapter mAdapter;

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

        mAdapter = new WeatherDetailAdapter();
        mRecyclerView = getRecycleView(mAdapter, R.id.recyclerView_weather_forecast_detail,
                mRecyclerItemTouchListener);

        if (bundle != null) {
            String weatherElement = bundle.getString("weatherElement");
            String locationName = bundle.getString("locationName");
            setWeatherForecastTime(weatherElement, locationName);
            mAdapter.setTitleArray(getResources().getStringArray(R.array.weather_title_array));
            mAdapter.setJsonArray(weatherElement);
            mAdapter.notifyDataSetChanged();
        }

    }

    private void setWeatherForecastTime(String result, String locationName) {

        TextView locationTextView = (TextView) findViewById(R.id.location_name);
        locationTextView.setText(locationName);

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(result);
            JSONObject weatherElement = (JSONObject) jsonArray.get(0);
            JSONArray timeArray = weatherElement.getJSONArray("time");
            for (int j = 0; j < timeArray.length(); j++) {
                String startTime = timeArray.getJSONObject(j).getString("startTime");
                startTime = startTime.substring(5, startTime.length() - 3);
                String endTime = timeArray.getJSONObject(j).getString("endTime");
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private RecyclerView getRecycleView(WeatherDetailAdapter adapter, int recyclerViewId, RecyclerItemTouchListener.OnItemClickListener listener) {
        // use a recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(recyclerViewId);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(
                getApplicationContext(), listener));

        return recyclerView;
    }

    public View.OnClickListener mButtonListener = v -> {
        setContentView(R.layout.activity_main);
        onBackPressed();
    };

    public RecyclerItemTouchListener.OnItemClickListener
            mRecyclerItemTouchListener =
            new RecyclerItemTouchListener.OnItemClickListener() {

                @Override
                public void onItemClick(int position) {

                }

                @Override
                public void onItemLongPress(int position) {
                }
            };

}

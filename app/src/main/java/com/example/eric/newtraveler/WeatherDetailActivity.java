package com.example.eric.newtraveler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.eric.newtraveler.adapter.RecyclerItemTouchListener;
import com.example.eric.newtraveler.adapter.WeatherDetailAdapter;

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
            String result = bundle.getString("weatherElement");
            mAdapter.setJsonArray(result);
            mAdapter.notifyDataSetChanged();
        }

    }

    private RecyclerView getRecycleView(WeatherDetailAdapter adapter, int recyclerViewId,
            RecyclerItemTouchListener.OnItemClickListener listener) {
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

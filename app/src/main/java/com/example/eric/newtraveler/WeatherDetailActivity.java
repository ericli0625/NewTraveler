package com.example.eric.newtraveler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.eric.newtraveler.adapter.BaseAdapter;
import com.example.eric.newtraveler.adapter.RecyclerItemTouchListener;
import com.example.eric.newtraveler.adapter.WeatherDetailAdapter;

public class WeatherDetailActivity extends AppCompatActivity {

    private String mName;
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
            String id = bundle.getString("id");
            mName = bundle.getString("name");
            String category = bundle.getString("category");
            String address = bundle.getString("address");
            String telephone = bundle.getString("telephone");
            String content = bundle.getString("content");

            TextView textViewNameDetail = (TextView) findViewById(R.id.textView_name_detail);
            TextView textViewCategoryDetail = (TextView) findViewById(R.id.textView_category_detail);
            TextView textViewAddressDetail = (TextView) findViewById(R.id.textView_address_detail);
            TextView textViewTelephoneDetail = (TextView) findViewById(R.id.textView_telephone_detail);
            TextView textViewContentDetail = (TextView) findViewById(R.id.textView_content_detail);

            textViewNameDetail.setText(mName);
            textViewCategoryDetail.setText(category);
            textViewAddressDetail.setText(address);
            textViewTelephoneDetail.setText(telephone);
            textViewContentDetail.setText(content);

        }

    }

    private RecyclerView getRecycleView(BaseAdapter adapter, int recyclerViewId,
                                        RecyclerItemTouchListener.OnItemClickListener listener) {
        // use a recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(recyclerViewId);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(
                getApplicationContext(), listener));

        return recyclerView;
    }

    public View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_main);
            onBackPressed();
        }
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

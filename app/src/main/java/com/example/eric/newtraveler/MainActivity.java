package com.example.eric.newtraveler;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eric.newtraveler.adapter.BaseAdapter;
import com.example.eric.newtraveler.adapter.FavoriteListAdapter;
import com.example.eric.newtraveler.adapter.NormalListAdapter;
import com.example.eric.newtraveler.adapter.SpotDetailAdapter;
import com.example.eric.newtraveler.database.SQLiteManager;
import com.example.eric.newtraveler.mvp.IBaseAdapterClickListener;
import com.example.eric.newtraveler.mvp.IMainView;
import com.example.eric.newtraveler.mvp.IPresenter;
import com.example.eric.newtraveler.widget.CustomItemDecoration;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IMainView {

    public final static String TAG = "Travel";
    private final static String PREFS_NAME = "TravelPrefsFile";
    private static final int IN_COUNTY_LIST_PAGE = 0;
    private static final int IN_CITY_LIST_PAGE = 1;
    private static final int IN_SPOT_LIST_PAGE = 2;

    private IPresenter mPresenter;

    private TextView mTextView;
    private EditText mEditText;
    private Toast mToast;

    private RecyclerView mRecyclerView;
    private CustomItemDecoration mCustomItemDecoration;

    private int mTriggerListLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadNormalCountySearchView();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Repository repository = new Repository(sharedPreferences);
        SQLiteManager.createInstance(this);

        mPresenter = new Presenter(this, repository);
        mPresenter.preloadAllCountyAndCityList();
    }

    public void loadCommonView() {
        ImageButton normalSearchModeButton = (ImageButton) findViewById(R.id.normal_search);
        ImageButton keywordSearchModeButton = (ImageButton) findViewById(R.id.keyword_search);
        ImageButton weatherForecastButton = (ImageButton) findViewById(R.id.weather_forecast);
        ImageButton favoriteListButton = (ImageButton) findViewById(R.id.favorite_list);
        normalSearchModeButton.setOnClickListener(mNormalSearchModeButtonListener);
        keywordSearchModeButton.setOnClickListener(mKeywordSearchModeButtonListener);
        weatherForecastButton.setOnClickListener(mWeatherForecastButtonListener);
        favoriteListButton.setOnClickListener(mFavoriteListButtonListener);
    }

    private RecyclerView getRecycleView(int recyclerViewId) {
        // use a recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(recyclerViewId);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a grid layout manager
        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);
        mCustomItemDecoration = new CustomItemDecoration(ContextCompat.getColor(this, R.color.colorGray));
        recyclerView.addItemDecoration(mCustomItemDecoration);

        return recyclerView;
    }

    public void loadNormalCountySearchView() {
        loadCommonView();
        mRecyclerView = getRecycleView(R.id.recyclerView);
        mTriggerListLevel = IN_COUNTY_LIST_PAGE;
    }

    public void loadNormalCitySearchView() {
        loadCommonView();
        mRecyclerView.removeItemDecoration(mCustomItemDecoration);
        mRecyclerView = getRecycleView(R.id.recyclerView);

        ImageButton returnIcon = (ImageButton) findViewById(R.id.return_icon);
        returnIcon.setVisibility(View.VISIBLE);
        returnIcon.setOnClickListener(mNormalSearchModeButtonListener);
        mTriggerListLevel = IN_CITY_LIST_PAGE;
    }

    public void loadNormalSpotSearch() {
        loadCommonView();
        mRecyclerView.removeItemDecoration(mCustomItemDecoration);
        mRecyclerView = getRecycleView(R.id.recyclerView);

        mTextView = (TextView) findViewById(R.id.list_title);
        mTextView.setText(R.string.chose_spot_title);

        ImageButton returnIcon = (ImageButton) findViewById(R.id.return_icon);
        returnIcon.setVisibility(View.VISIBLE);
        returnIcon.setOnClickListener(mBackToCityListPageListener);
        mTriggerListLevel = IN_SPOT_LIST_PAGE;
    }

    public void loadKeyWordSearchView() {
        loadCommonView();
        mRecyclerView = getRecycleView(R.id.recyclerView_keyword_search);

        mEditText = (EditText) findViewById(R.id.editText);

        Button keywordSearchButton = (Button) findViewById(R.id.keyword_search_button);
        keywordSearchButton.setOnClickListener(mKeywordSearchButtonListener);
    }

    private void loadWeatherForecastView() {
        loadCommonView();
        mRecyclerView = getRecycleView(R.id.recyclerView_weather_forecast_city);
    }

    private void loadFavoriteListView() {
        loadCommonView();
        mRecyclerView = getRecycleView(R.id.recyclerView);

        mTextView = (TextView) findViewById(R.id.list_title);
        mTextView.setText(R.string.favorite_title);
    }

    public View.OnClickListener mNormalSearchModeButtonListener = v -> {
        setContentView(R.layout.activity_main);
        loadNormalCountySearchView();

        mPresenter.showCountyList();
    };

    public View.OnClickListener mBackToCityListPageListener = v -> {
        setContentView(R.layout.activity_main);
        loadNormalCitySearchView();

        mPresenter.backToCityListPage();
    };

    public View.OnClickListener mKeywordSearchModeButtonListener = v -> {
        setContentView(R.layout.keyword_search_layout);
        loadKeyWordSearchView();
    };

    public View.OnClickListener mKeywordSearchButtonListener = v -> {
        showToast(true, Toast.LENGTH_LONG);

        mPresenter.showKeywordSearchSpot(mEditText.getText().toString());
    };

    public View.OnClickListener mWeatherForecastButtonListener = v -> {
        setContentView(R.layout.weather_layout);
        loadWeatherForecastView();

        mPresenter.showWeatherCountyList();
    };

    public View.OnClickListener mFavoriteListButtonListener = v -> {
        setContentView(R.layout.activity_main);
        loadFavoriteListView();

        mPresenter.showFavoriteList();
    };

    @Override
    public void showCountyListResult(ArrayList<String> arrayList) {
        Log.v(MainActivity.TAG, "MainActivity, showCountyListResult ");

        // set result data to an adapter
        BaseAdapter adapter = new NormalListAdapter(arrayList, mNormalListRecyclerItemTouchListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showCityListResult(ArrayList<String> arrayList) {
        Log.v(MainActivity.TAG, "MainActivity, showCityListResult ");

        // set result data to an adapter
        BaseAdapter adapter = new NormalListAdapter(arrayList, mNormalListRecyclerItemTouchListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);

        showToast(false, Toast.LENGTH_LONG);
    }

    @Override
    public void showSpotListResult(ArrayList<String> arrayList) {
        Log.v(MainActivity.TAG, "MainActivity, showSpotListResult ");

        // set result data to an adapter
        BaseAdapter adapter = new SpotDetailAdapter(arrayList, mNormalListRecyclerItemTouchListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);

        showToast(false, Toast.LENGTH_LONG);
    }

    @Override
    public void showKeywordSearchSpotResult(ArrayList<String> arrayList) {
        Log.v(MainActivity.TAG, "MainActivity, showKeywordSearchSpotResult ");

        // set result data to an adapter
        BaseAdapter adapter = new SpotDetailAdapter(arrayList, mKeywordSearchSpotRecyclerItemTouchListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);

        showToast(false, Toast.LENGTH_LONG);
    }

    @Override
    public void showSpotDetailResult(Bundle bundle) {
        Log.v(MainActivity.TAG, "MainActivity, showSpotDetailResult ");

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), SpotDetailActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    @Override
    public void showWeatherCountyListResult(ArrayList<String> arrayList) {
        Log.v(MainActivity.TAG, "MainActivity, showWeatherCountyListResult");

        // set result data to an adapter
        BaseAdapter adapter = new NormalListAdapter(arrayList, mWeatherForecastRecyclerItemTouchListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showWeatherForecastResult(Bundle bundle) {
        Log.v(MainActivity.TAG, "MainActivity, showWeatherForecastResult");

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), WeatherDetailActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    @Override
    public void showFavoriteListResult(ArrayList<String> arrayList) {
        Log.v(MainActivity.TAG, "MainActivity, showFavoriteListResult");

        // set result data to an adapter
        BaseAdapter adapter = new FavoriteListAdapter(arrayList, mFavoriteListRecyclerItemTouchListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showDeleteFavoriteResult(ArrayList<String> arrayList) {
        Log.v(MainActivity.TAG, "MainActivity, showDeleteFavoriteResult");

        showFavoriteListResult(arrayList);
    }

    public IBaseAdapterClickListener mNormalListRecyclerItemTouchListener = new IBaseAdapterClickListener() {

        @Override
        public void onItemClick(String result) {
            switch (mTriggerListLevel) {
                case IN_COUNTY_LIST_PAGE:
                    loadNormalCitySearchView();
                    showToast(true, Toast.LENGTH_LONG);

                    mPresenter.showCityList(result);
                    break;
                case IN_CITY_LIST_PAGE:
                    loadNormalSpotSearch();
                    showToast(true, Toast.LENGTH_LONG);

                    mRecyclerView.setAdapter(null);

                    mPresenter.showSpotList(result);
                    break;
                case IN_SPOT_LIST_PAGE:
                    mPresenter.showSpotDetail(result);
                    break;
            }
        }

        @Override
        public boolean onItemLongClick(String result) {
            return false;
        }
    };

    public IBaseAdapterClickListener mKeywordSearchSpotRecyclerItemTouchListener = new IBaseAdapterClickListener() {

        @Override
        public void onItemClick(String spotName) {
            showToast(true, Toast.LENGTH_LONG);

            mPresenter.showSpotDetail(spotName);
        }

        @Override
        public boolean onItemLongClick(String result) {
            return false;
        }
    };

    public IBaseAdapterClickListener mWeatherForecastRecyclerItemTouchListener = new IBaseAdapterClickListener() {

        @Override
        public void onItemClick(String countyName) {
            mPresenter.showWeatherForecast(countyName);
        }

        @Override
        public boolean onItemLongClick(String result) {
            return false;
        }
    };

    public IBaseAdapterClickListener mFavoriteListRecyclerItemTouchListener = new IBaseAdapterClickListener() {

        @Override
        public void onItemClick(String spotName) {
            mPresenter.showFavoriteSpotDetail(spotName);
        }

        @Override
        public boolean onItemLongClick(String spotName) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.delete_dialog_title)
                    .setMessage(R.string.delete_dialog_message)
                    .setPositiveButton(R.string.delete_dialog_ok, (dialog, id) ->
                            mPresenter.deleteFavoriteSpot(spotName))
                    .setNegativeButton(R.string.delete_dialog_cancel, (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
    };

    private void showToast(boolean isShow, int toastDuration) {
        if (isShow) {
            mToast = Toast.makeText(MainActivity.this, R.string.toast_searching, toastDuration);
            mToast.show();
        } else {
            mToast.cancel();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_content)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_dialog_yes, (dialog, id) -> MainActivity.this.finish())
                .setNegativeButton(R.string.alert_dialog_no, (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTriggerListLevel = 0;
    }
}

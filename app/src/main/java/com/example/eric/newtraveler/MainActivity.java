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
import com.example.eric.newtraveler.widget.CustomItemDecoration;

public class MainActivity extends AppCompatActivity implements IMainView {

    public final static String TAG = "Travel";
    private final static String PREFS_NAME = "TravelPrefsFile";
    private static final int IN_COUNTY_LIST_PAGE = 0;
    private static final int IN_CITY_LIST_PAGE = 1;
    private static final int IN_SPOT_LIST_PAGE = 2;

    private Presenter mPresenter;

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

    public View.OnClickListener mNormalSearchModeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_main);
            loadNormalCountySearchView();

            mPresenter.showCountyList();
        }
    };

    public View.OnClickListener mBackToCityListPageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_main);
            loadNormalCitySearchView();

            mPresenter.backToCityListPage();
        }
    };

    public View.OnClickListener mKeywordSearchModeButtonListener = v -> {
        setContentView(R.layout.keyword_search_layout);
        loadKeyWordSearchView();
    };

    public View.OnClickListener mKeywordSearchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showToast(true, Toast.LENGTH_LONG);

            mPresenter.showKeywordSearchSpot(mEditText.getText().toString());
        }
    };

    public View.OnClickListener mWeatherForecastButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.weather_layout);
            loadWeatherForecastView();

            mPresenter.showWeatherCountyList();
        }
    };

    public View.OnClickListener mFavoriteListButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_main);
            loadFavoriteListView();

            mPresenter.showFavoriteList();
        }
    };

    @Override
    public void showCountyListResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showCountyListResult ");

        // set result data to an adapter
        BaseAdapter adapter = new NormalListAdapter(string, mNormalListRecyclerItemTouchListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showCityListResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showCityListResult ");

        // set result data to an adapter
        BaseAdapter adapter = new NormalListAdapter(string, mNormalListRecyclerItemTouchListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);

        showToast(false, Toast.LENGTH_LONG);
    }

    @Override
    public void showSpotListResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showSpotListResult ");

        // set result data to an adapter
        BaseAdapter adapter = new SpotDetailAdapter(string, mNormalListRecyclerItemTouchListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);

        showToast(false, Toast.LENGTH_LONG);
    }

    @Override
    public void showKeywordSearchSpotResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showKeywordSearchSpotResult ");

        // set result data to an adapter
        BaseAdapter adapter = new SpotDetailAdapter(string, mKeywordSearchSpotRecyclerItemTouchListener);
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
    public void showWeatherCountyListResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showWeatherCountyListResult");

        // set result data to an adapter
        BaseAdapter adapter = new NormalListAdapter(string, mWeatherForecastRecyclerItemTouchListener);
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
    public void showFavoriteListResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showFavoriteListResult");

        // set result data to an adapter
        BaseAdapter adapter = new FavoriteListAdapter(string, mFavoriteListRecyclerItemTouchListener);
        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showDeleteFavoriteResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showDeleteFavoriteResult");

        showFavoriteListResult(string);
    }

    public IBaseAdapterClickListener mNormalListRecyclerItemTouchListener = new IBaseAdapterClickListener() {

        @Override
        public void onItemClick(String string, int position) {
            switch (mTriggerListLevel) {
                case IN_COUNTY_LIST_PAGE:
                    loadNormalCitySearchView();
                    showToast(true, Toast.LENGTH_LONG);

                    mPresenter.showCityList(string, position);
                    break;
                case IN_CITY_LIST_PAGE:
                    loadNormalSpotSearch();
                    showToast(true, Toast.LENGTH_LONG);

                    mPresenter.showSpotList(string, position);
                    break;
                case IN_SPOT_LIST_PAGE:
                    mPresenter.showSpotDetail(string, position);
                    break;
            }
        }

        @Override
        public boolean onItemLongClick(String string, int position) {
            return false;
        }
    };

    public IBaseAdapterClickListener mKeywordSearchSpotRecyclerItemTouchListener = new IBaseAdapterClickListener() {

        @Override
        public void onItemClick(String string, int position) {
            mPresenter.showSpotDetail(string, position);
        }

        @Override
        public boolean onItemLongClick(String string, int position) {
            return false;
        }
    };

    public IBaseAdapterClickListener mWeatherForecastRecyclerItemTouchListener = new IBaseAdapterClickListener() {

        @Override
        public void onItemClick(String string, int position) {
            mPresenter.showWeatherForecast(string, position);
        }

        @Override
        public boolean onItemLongClick(String string, int position) {
            return false;
        }
    };

    public IBaseAdapterClickListener mFavoriteListRecyclerItemTouchListener = new IBaseAdapterClickListener() {

        @Override
        public void onItemClick(String string, int position) {
            mPresenter.showFavoriteSpotDetail(string, position);
        }

        @Override
        public boolean onItemLongClick(String string, int position) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.delete_dialog_title)
                    .setMessage(R.string.delete_dialog_message)
                    .setPositiveButton(R.string.delete_dialog_ok, (dialog, id) ->
                            mPresenter.deleteFavoriteSpot(string, position))
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

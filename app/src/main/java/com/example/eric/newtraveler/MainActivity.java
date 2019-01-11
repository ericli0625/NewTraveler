package com.example.eric.newtraveler;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
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
import com.example.eric.newtraveler.adapter.RecyclerItemTouchListener;
import com.example.eric.newtraveler.adapter.SpotDetailAdapter;
import com.example.eric.newtraveler.database.SQLiteManager;
import com.example.eric.newtraveler.mvp.IMainView;

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
    private BaseAdapter mNormalListAdapter;
    private BaseAdapter mSpotDetailAdapter;
    private FavoriteListAdapter mFavoriteListAdapter;

    private String mStringKeywordSearchSpotResult;
    private String mStringNormalListResult;
    private String mStringFavoriteListResult;

    private int mTriggerListLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadCommonView();
        mNormalListAdapter = new NormalListAdapter();
        mRecyclerView = getRecycleView(mNormalListAdapter, R.id.recyclerView,
                mNormalListRecyclerItemTouchListener);

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

        recyclerView.addItemDecoration(mItemDecoration);
        recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(
                getApplicationContext(), listener));

        return recyclerView;
    }

    RecyclerView.ItemDecoration mItemDecoration = new RecyclerView.ItemDecoration() {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(10, 0, 0, 10);
        }
    };

    public void loadNormalCountySearchView() {
        loadCommonView();
        mNormalListAdapter = new NormalListAdapter();
        mRecyclerView = getRecycleView(mNormalListAdapter, R.id.recyclerView,
                mNormalListRecyclerItemTouchListener);
        mTriggerListLevel = IN_COUNTY_LIST_PAGE;
    }

    public void loadNormalCitySearchView() {
        loadCommonView();
        mNormalListAdapter = new NormalListAdapter();
        mRecyclerView.removeItemDecoration(mItemDecoration);
        mRecyclerView = getRecycleView(mNormalListAdapter, R.id.recyclerView,
                mNormalListRecyclerItemTouchListener);

        ImageButton returnIcon = (ImageButton) findViewById(R.id.return_icon);
        returnIcon.setVisibility(View.VISIBLE);
        returnIcon.setOnClickListener(mNormalSearchModeButtonListener);
        mTriggerListLevel = IN_CITY_LIST_PAGE;
    }

    public void loadNormalSpotSearch() {
        loadCommonView();
        mSpotDetailAdapter = new SpotDetailAdapter();
        mRecyclerView.removeItemDecoration(mItemDecoration);
        mRecyclerView = getRecycleView(mSpotDetailAdapter, R.id.recyclerView,
                mNormalListRecyclerItemTouchListener);

        mTextView = (TextView) findViewById(R.id.list_title);
        mTextView.setText(R.string.chose_spot_title);

        ImageButton returnIcon = (ImageButton) findViewById(R.id.return_icon);
        returnIcon.setVisibility(View.VISIBLE);
        returnIcon.setOnClickListener(mBackToCityListPageListener);
        mTriggerListLevel = IN_SPOT_LIST_PAGE;
    }

    public void loadKeyWordSearchView() {
        loadCommonView();
        mSpotDetailAdapter = new SpotDetailAdapter();
        mRecyclerView = getRecycleView(mSpotDetailAdapter, R.id.recyclerView_keyword_search,
                mKeywordSearchSpotRecyclerItemTouchListener);

        mEditText = (EditText) findViewById(R.id.editText);

        Button keywordSearchButton = (Button) findViewById(R.id.keyword_search_button);
        keywordSearchButton.setOnClickListener(mKeywordSearchButtonListener);
    }

    private void loadWeatherForecastView() {
        loadCommonView();
        mNormalListAdapter = new NormalListAdapter();
        mRecyclerView = getRecycleView(mNormalListAdapter, R.id.recyclerView_weather_forecast_city,
                mWeatherForecastRecyclerItemTouchListener);
    }

    private void loadFavoriteListView() {
        loadCommonView();
        mFavoriteListAdapter = new FavoriteListAdapter();
        mRecyclerView = getRecycleView(mFavoriteListAdapter, R.id.recyclerView,
                mFavoriteListRecyclerItemTouchListener);

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

            mPresenter.showCountyList();
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
        mNormalListAdapter.setJsonArray(string);
        mNormalListAdapter.notifyDataSetChanged();
        mStringNormalListResult = string;
    }

    @Override
    public void showCityListResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showCityListResult ");

        // set result data to an adapter
        mNormalListAdapter.setJsonArray(string);
        mNormalListAdapter.notifyDataSetChanged();
        mStringNormalListResult = string;

        showToast(false, Toast.LENGTH_LONG);
    }

    @Override
    public void showSpotListResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showSpotListResult ");

        // set result data to an adapter
        mSpotDetailAdapter.setJsonArray(string);
        mSpotDetailAdapter.notifyDataSetChanged();
        mStringNormalListResult = string;

        showToast(false, Toast.LENGTH_LONG);
    }

    @Override
    public void showKeywordSearchSpotResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showKeywordSearchSpotResult ");

        // set result data to an adapter
        mSpotDetailAdapter.setJsonArray(string);
        mSpotDetailAdapter.notifyDataSetChanged();
        mStringKeywordSearchSpotResult = string;

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
        mFavoriteListAdapter.setJsonArray(string);
        mFavoriteListAdapter.notifyDataSetChanged();
        mStringFavoriteListResult = string;

    }

    public RecyclerItemTouchListener.OnItemClickListener
            mNormalListRecyclerItemTouchListener =
            new RecyclerItemTouchListener.OnItemClickListener() {

                @Override
                public void onItemClick(int position) {

                    switch (mTriggerListLevel) {
                        case IN_COUNTY_LIST_PAGE:
                            loadNormalCitySearchView();
                            showToast(true, Toast.LENGTH_LONG);

                            mPresenter.showCityList(mStringNormalListResult, position);
                            break;
                        case IN_CITY_LIST_PAGE:
                            loadNormalSpotSearch();
                            showToast(true, Toast.LENGTH_LONG);

                            mPresenter.showSpotList(mStringNormalListResult, position);
                            break;
                        case IN_SPOT_LIST_PAGE:
                            mPresenter.showSpotDetail(mStringNormalListResult, position);
                            break;
                    }
                }

                @Override
                public void onItemLongPress(int position) {
                }
            };

    public RecyclerItemTouchListener.OnItemClickListener
            mKeywordSearchSpotRecyclerItemTouchListener =
            new RecyclerItemTouchListener.OnItemClickListener() {

                @Override
                public void onItemClick(int position) {
                    mPresenter.showSpotDetail(mStringKeywordSearchSpotResult, position);
                }

                @Override
                public void onItemLongPress(int position) {
                }
            };

    public RecyclerItemTouchListener.OnItemClickListener
            mWeatherForecastRecyclerItemTouchListener =
            new RecyclerItemTouchListener.OnItemClickListener() {

                @Override
                public void onItemClick(int position) {
                    mPresenter.showWeatherForecast(mStringNormalListResult, position);
                }

                @Override
                public void onItemLongPress(int position) {
                }
            };

    public RecyclerItemTouchListener.OnItemClickListener
            mFavoriteListRecyclerItemTouchListener =
            new RecyclerItemTouchListener.OnItemClickListener() {

                @Override
                public void onItemClick(int position) {
                    mPresenter.showFavoriteSpotDetail(mStringFavoriteListResult, position);
                }

                @Override
                public void onItemLongPress(int position) {
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

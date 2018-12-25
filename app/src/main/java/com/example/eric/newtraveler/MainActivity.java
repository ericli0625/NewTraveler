package com.example.eric.newtraveler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.eric.newtraveler.adapter.BaseAdapter;
import com.example.eric.newtraveler.adapter.CityListAdapter;
import com.example.eric.newtraveler.adapter.KeywordSearchSpotAdapter;
import com.example.eric.newtraveler.adapter.RecyclerItemTouchListener;
import com.example.eric.newtraveler.view.IMainView;

public class MainActivity extends AppCompatActivity implements IMainView {

    public final static String TAG = "Travel";
    private final static String PREFS_NAME = "TravelPrefsFile";

    private Presenter mPresenter;

    private EditText mEditText;
    private Toast mToast;

    private RecyclerView mRecyclerView;
    private BaseAdapter mNormalListAdapter;
    private BaseAdapter mKeywordSearchSpotAdapter;

    private String mStringKeywordSearchSpotResult;
    private String mStringNormalListResult;

    private int mTriggerListLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadCommonView();
        mNormalListAdapter = new CityListAdapter();
        mRecyclerView = getRecycleView(mNormalListAdapter, R.id.recyclerView, mCityListRecyclerItemTouchListener);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        mPresenter = new Presenter(this, sharedPreferences);
        mPresenter.showCityList();
    }

    public void loadCommonView() {
        ImageButton normalSearchModeButton = (ImageButton) findViewById(R.id.normal_search);
        ImageButton keywordSearchModeButton = (ImageButton) findViewById(R.id.keyword_search);
        normalSearchModeButton.setOnClickListener(mNormalSearchModeButtonListener);
        keywordSearchModeButton.setOnClickListener(mKeywordSearchModeButtonListener);
    }

    private RecyclerView getRecycleView(BaseAdapter adapter, int recyclerViewId,
            RecyclerItemTouchListener.OnItemClickListener listener) {
        // use a recycler view
        RecyclerView recyclerView = (RecyclerView) findViewById(recyclerViewId);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(
                getApplicationContext(), listener));

        return recyclerView;
    }

    public void loadNormalCitySearch() {
        loadCommonView();
        mNormalListAdapter = new CityListAdapter();
        mRecyclerView = getRecycleView(mNormalListAdapter, R.id.recyclerView, mCityListRecyclerItemTouchListener);
        mTriggerListLevel = 0;
    }

    public void loadNormalCountySearch() {
        loadCommonView();
        mNormalListAdapter = new CityListAdapter();
        mRecyclerView = getRecycleView(mNormalListAdapter, R.id.recyclerView, mCityListRecyclerItemTouchListener);

        ImageButton returnIcon = (ImageButton) findViewById(R.id.return_icon);
        returnIcon.setVisibility(View.VISIBLE);
        returnIcon.setOnClickListener(mNormalSearchModeButtonListener);
        mTriggerListLevel = 1;
    }

    public void loadNormalSpotSearch() {
        loadCommonView();
        mNormalListAdapter = new CityListAdapter();
        mRecyclerView = getRecycleView(mNormalListAdapter, R.id.recyclerView, mCityListRecyclerItemTouchListener);

        ImageButton returnIcon = (ImageButton) findViewById(R.id.return_icon);
        returnIcon.setVisibility(View.VISIBLE);
        returnIcon.setOnClickListener(mNormalSearchModeButtonListener);
        mTriggerListLevel = 2;
    }

    public void loadKeyWordSearchView() {
        loadCommonView();
        mKeywordSearchSpotAdapter = new KeywordSearchSpotAdapter();
        mRecyclerView = getRecycleView(mKeywordSearchSpotAdapter, R.id.recyclerView_keyword_search, mKeywordSearchSpotRecyclerItemTouchListener);

        mEditText = (EditText) findViewById(R.id.editText);

        Button keywordSearchButton = (Button) findViewById(R.id.keyword_search_button);
        keywordSearchButton.setOnClickListener(mKeywordSearchButtonListener);
    }

    public View.OnClickListener mNormalSearchModeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_main);
            loadNormalCitySearch();

            mPresenter.showCityList();
        }
    };

    public View.OnClickListener mKeywordSearchModeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.keyword_search_layout);
            loadKeyWordSearchView();
        }
    };

    public View.OnClickListener mKeywordSearchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showToast(true);

            mPresenter.showKeywordSearchSpot(mEditText.getText().toString());
        }
    };

    @Override
    public void showCityListResult(String string) {
        // set result data to an adapter
        mNormalListAdapter.setJsonArray(string);
        mNormalListAdapter.notifyDataSetChanged();
        mStringNormalListResult = string;
    }

    @Override
    public void showCountyListResult(String string) {
        // set result data to an adapter
        mNormalListAdapter.setJsonArray(string);
        mNormalListAdapter.notifyDataSetChanged();
        mStringNormalListResult = string;

        showToast(false);
    }

    @Override
    public void showSpotListResult(String string) {
        // set result data to an adapter
        mNormalListAdapter.setJsonArray(string);
        mNormalListAdapter.notifyDataSetChanged();
        mStringNormalListResult = string;

        showToast(false);
    }

    @Override
    public void showKeywordSearchSpotResult(String string) {
        // set result data to an adapter
        mKeywordSearchSpotAdapter.setJsonArray(string);
        mKeywordSearchSpotAdapter.notifyDataSetChanged();
        mStringKeywordSearchSpotResult = string;

        showToast(false);
    }

    @Override
    public void showSpotDetailResult(Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), SpotDetailActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getApplicationContext().startActivity(intent);
    }

    public RecyclerItemTouchListener.OnItemClickListener
            mCityListRecyclerItemTouchListener =
            new RecyclerItemTouchListener.OnItemClickListener() {

                @Override
                public void onItemClick(int position) {
                    Log.v(MainActivity.TAG, "BaseAdapter, onItemClick " + position + " mTriggerListLevel =" + mTriggerListLevel);
                    showToast(true);

                    switch (mTriggerListLevel) {
                        case 0:
                            loadNormalCountySearch();
                            mPresenter.showCountyList(mStringNormalListResult, position);
                            break;
                        case 1:
                            loadNormalSpotSearch();
                            mPresenter.showSpotList(mStringNormalListResult, position);
                            break;
                        case 2:
                            mPresenter.showSpotDetail(mStringNormalListResult, position);
                            break;
                    }
                }

                @Override
                public void onItemLongPress(int position) {
                    Log.v(MainActivity.TAG, "BaseAdapter, onItemLongPress " + position);
                }
            };

    public RecyclerItemTouchListener.OnItemClickListener
            mKeywordSearchSpotRecyclerItemTouchListener =
            new RecyclerItemTouchListener.OnItemClickListener() {

                @Override
                public void onItemClick(int position) {
                    Log.v(MainActivity.TAG, "BaseAdapter, onItemClick " + position);
                    mPresenter.showSpotDetail(mStringKeywordSearchSpotResult, position);
                }

                @Override
                public void onItemLongPress(int position) {
                    Log.v(MainActivity.TAG, "BaseAdapter, onItemLongPress " + position);
                }
            };

    private void showToast(boolean isShow) {
        if (isShow) {
            mToast = Toast.makeText(MainActivity.this, R.string.toast_searching, Toast.LENGTH_LONG);
            mToast.show();
        } else {
            mToast.cancel();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTriggerListLevel = 0;
    }
}

package com.example.eric.newtraveler;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.eric.newtraveler.adapter.BaseAdapter;
import com.example.eric.newtraveler.adapter.NormalListAdapter;
import com.example.eric.newtraveler.adapter.RecyclerItemTouchListener;
import com.example.eric.newtraveler.adapter.SpotDetailAdapter;
import com.example.eric.newtraveler.view.IMainView;

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

    private String mStringKeywordSearchSpotResult;
    private String mStringNormalListResult;

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
        mPresenter = new Presenter(this, repository);
        mPresenter.preloadAllCountyAndCityList();
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

    public void loadNormalCountySearch() {
        loadCommonView();
        mNormalListAdapter = new NormalListAdapter();
        mRecyclerView = getRecycleView(mNormalListAdapter, R.id.recyclerView,
                mNormalListRecyclerItemTouchListener);
        mTriggerListLevel = IN_COUNTY_LIST_PAGE;
    }

    public void loadNormalCitySearch() {
        loadCommonView();
        mNormalListAdapter = new NormalListAdapter();
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

    public View.OnClickListener mNormalSearchModeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_main);
            loadNormalCountySearch();

            mPresenter.showCountyList();
        }
    };

    public View.OnClickListener mBackToCityListPageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_main);
            loadNormalCitySearch();

            mPresenter.backToCityListPage();
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

        showToast(false);
    }

    @Override
    public void showSpotListResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showSpotListResult ");

        // set result data to an adapter
        mSpotDetailAdapter.setJsonArray(string);
        mSpotDetailAdapter.notifyDataSetChanged();
        mStringNormalListResult = string;

        showToast(false);
    }

    @Override
    public void showKeywordSearchSpotResult(String string) {
        Log.v(MainActivity.TAG, "MainActivity, showKeywordSearchSpotResult ");

        // set result data to an adapter
        mSpotDetailAdapter.setJsonArray(string);
        mSpotDetailAdapter.notifyDataSetChanged();
        mStringKeywordSearchSpotResult = string;

        showToast(false);
    }

    @Override
    public void showSpotDetailResult(Bundle bundle) {
        Log.v(MainActivity.TAG, "MainActivity, showSpotDetailResult ");

        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), SpotDetailActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getApplicationContext().startActivity(intent);
    }

    public RecyclerItemTouchListener.OnItemClickListener
            mNormalListRecyclerItemTouchListener =
            new RecyclerItemTouchListener.OnItemClickListener() {

                @Override
                public void onItemClick(int position) {

                    switch (mTriggerListLevel) {
                        case IN_COUNTY_LIST_PAGE:
                            loadNormalCitySearch();
                            showToast(true);

                            mPresenter.showCityList(mStringNormalListResult, position);
                            break;
                        case IN_CITY_LIST_PAGE:
                            loadNormalSpotSearch();
                            showToast(true);

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

    private void showToast(boolean isShow) {
        if (isShow) {
            mToast = Toast.makeText(MainActivity.this, R.string.toast_searching, Toast.LENGTH_SHORT);
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
                .setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTriggerListLevel = 0;
    }
}

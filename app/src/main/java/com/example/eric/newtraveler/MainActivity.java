package com.example.eric.newtraveler;

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

    private Presenter mPresenter;

    private EditText mEditText;
    private Toast mToastSearching;

    private RecyclerView mRecyclerView;
    private CityListAdapter mCityListAdapter;
    private KeywordSearchSpotAdapter mKeywordSearchSpotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadInitCommonView();
        mCityListAdapter = new CityListAdapter();
        mRecyclerView = getRecycleView(mCityListAdapter, R.id.recyclerView);

        mPresenter = new Presenter(this);
        mPresenter.showCityList();
    }

    public void loadInitCommonView() {

        ImageButton normalSearchModeButton = (ImageButton) findViewById(R.id.normal_search);
        ImageButton keywordSearchModeButton = (ImageButton) findViewById(R.id.keyword_search);
        normalSearchModeButton.setOnClickListener(mNormalSearchModeButtonListener);
        keywordSearchModeButton.setOnClickListener(mKeywordSearchModeButtonListener);

    }

    private RecyclerView getRecycleView(BaseAdapter adapter, int recyclerViewId) {
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
                getApplicationContext(), new BaseRecyclerItemTouchListener()));

        return recyclerView;
    }

    public View.OnClickListener mNormalSearchModeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_main);

            loadInitCommonView();
            mCityListAdapter = new CityListAdapter();
            mRecyclerView = getRecycleView(mCityListAdapter, R.id.recyclerView);

            mPresenter.showCityList();
        }
    };

    public View.OnClickListener mKeywordSearchModeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.keyword_search_layout);

            loadInitCommonView();
            mKeywordSearchSpotAdapter = new KeywordSearchSpotAdapter();
            mRecyclerView = getRecycleView(mKeywordSearchSpotAdapter,
                    R.id.recyclerView_keyword_search);

            mEditText = (EditText) findViewById(R.id.editText);

            Button keywordSearchButton = (Button) findViewById(R.id.keyword_search_button);
            keywordSearchButton.setOnClickListener(mKeywordSearchButtonListener);

        }
    };

    public View.OnClickListener mKeywordSearchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mToastSearching = Toast.makeText(MainActivity.this, R.string.toast_searching,
                    Toast.LENGTH_SHORT);
            mToastSearching.show();
            mPresenter.showKeywordSearchSpot(mEditText.getText().toString());
        }
    };

    @Override
    public void showCityListResult(String string) {
        // set result data to an adapter
        mCityListAdapter.setJsonArray(string);
        mCityListAdapter.notifyDataSetChanged();
    }

    @Override
    public void showKeywordSearchSpotResult(String string) {
        // set result data to an adapter
        mKeywordSearchSpotAdapter.setJsonArray(string);
        mKeywordSearchSpotAdapter.notifyDataSetChanged();
        mToastSearching.cancel();
    }

    public class BaseRecyclerItemTouchListener implements
            RecyclerItemTouchListener.OnItemClickListener {
        @Override
        public void onItemClick(int position) {
            Log.v(MainActivity.TAG, "BaseAdapter, onItemClick " + position);
        }

        @Override
        public void onItemLongPress(int position) {
            Log.v(MainActivity.TAG, "BaseAdapter, onItemLongPress " + position);
        }
    }

}

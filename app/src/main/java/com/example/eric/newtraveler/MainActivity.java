package com.example.eric.newtraveler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.eric.newtraveler.view.*;

public class MainActivity extends AppCompatActivity implements IMainView {

    public final static String TAG = "Travel";
    public final static int MSG_SHOW_CITY_LIST_RESULT = 1;
    public final static int MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT = 2;

    private Presenter mPresenter;

    private ImageButton mKeywordSearchModeButton;
    private ImageButton mNormalSearchModeButton;
    private EditText mEditText;
    private Button mKeywordSearchButton;
    private Toast mToastSearching;

    private RecyclerView mRecyclerView;
    private BaseAdapter mCityAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadInitCommonView();

        mPresenter = new Presenter(this);
        mPresenter.showCityList();
    }

    public void loadInitCommonView() {

        // use a recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mNormalSearchModeButton = (ImageButton) findViewById(R.id.normal_search);
        mKeywordSearchModeButton = (ImageButton) findViewById(R.id.keyword_search);
        mNormalSearchModeButton.setOnClickListener(mNormalSearchModeButtonListener);
        mKeywordSearchModeButton.setOnClickListener(mKeywordSearchModeButtonListener);

    }

    public View.OnClickListener mNormalSearchModeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_main);
            loadInitCommonView();

            mPresenter.showCityList();
        }
    };

    public View.OnClickListener mKeywordSearchModeButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.keyword_search_layout);
            loadInitCommonView();

            mEditText = (EditText) findViewById(R.id.editText);

            mKeywordSearchButton = (Button) findViewById(R.id.keyword_search_button);
            mKeywordSearchButton.setOnClickListener(mKeywordSearchButtonListener);
        }
    };

    public View.OnClickListener mKeywordSearchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mToastSearching = Toast.makeText(MainActivity.this, R.string.toast_searching, Toast.LENGTH_SHORT);
            mToastSearching.show();
            mPresenter.showKeywordSearchSpot(mEditText.getText().toString());
        }
    };

    @Override
    public void showCityListResult(String string) {
        Message msg = new Message();
        msg.what = MSG_SHOW_CITY_LIST_RESULT;
        msg.obj = string;
        mMainHandler.sendMessage(msg);
    }

    @Override
    public void showKeywordSearchSpotResult(String string) {
        Message msg = new Message();
        msg.what = MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT;
        msg.obj = string;
        mMainHandler.sendMessage(msg);
    }

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // Gets the image task from the incoming Message object.
            String string = msg.obj != null ? (String) msg.obj : "";
            int msgType = msg.what;
            switch (msgType) {
                case MSG_SHOW_CITY_LIST_RESULT:
                    // set result data to an adapter
                    mCityAdapter = new CityListAdapter(string);
                    mRecyclerView.setAdapter(mCityAdapter);
                    mRecyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(
                            getApplicationContext(), new BaseRecyclerItemTouchListener()));
                    break;
                case MSG_SHOW_KEYWORD_SEARCH_SPOT_RESULT:
                    // set result data to an adapter
                    mCityAdapter = new KeywordSearchSpotAdapter(string);
                    mRecyclerView.setAdapter(mCityAdapter);
                    mRecyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(
                            getApplicationContext(), new BaseRecyclerItemTouchListener()));
                    break;
                default:
                    break;
            }
        }
    };

    public class BaseRecyclerItemTouchListener implements RecyclerItemTouchListener.OnItemClickListener {
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

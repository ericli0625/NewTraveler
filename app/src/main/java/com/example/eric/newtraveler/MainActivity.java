package com.example.eric.newtraveler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.eric.newtraveler.adapter.CityAdapter;
import com.example.eric.newtraveler.view.*;

public class MainActivity extends AppCompatActivity implements IMainView {

    public final static String TAG = "Travel";

    public static final int MSG_SHOW_CITY_RESULT = 1;

    private Presenter mPresenter;
    private Button mButton;
    private TextView mTextView;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mCityAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new Presenter(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mButton = (Button) this.findViewById(R.id.click_button);
        mButton.setOnClickListener(mButtonListener);

        mTextView = (TextView) this.findViewById(R.id.textView);

    }

    public View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mPresenter.showAllCity();
        }
    };

    @Override
    public void showCityResult(String string) {
        Message msg = new Message();
        msg.what = MSG_SHOW_CITY_RESULT;
        msg.obj = string;
        mMainHandler.sendMessage(msg);
    }

    @Override
    public void ShowSpotResult() {

    }

    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // Gets the image task from the incoming Message object.
            switch (msg.what) {
                case MSG_SHOW_CITY_RESULT:
                    String string = (String) msg.obj;
                    mTextView.setText(string);

                    // set result data to an adapter
                    mCityAdapter = new CityAdapter(string);
                    mRecyclerView.setAdapter(mCityAdapter);

                    break;
                default:
                    break;
            }
        }
    };
}

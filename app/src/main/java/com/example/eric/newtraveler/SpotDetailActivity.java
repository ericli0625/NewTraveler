package com.example.eric.newtraveler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class SpotDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);
        Log.v(MainActivity.TAG, "SpotDetailActivity, onCreate ");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String id = bundle.getString("id");
            String name = bundle.getString("name");
            Log.v(MainActivity.TAG, "SpotDetailActivity, getExtras id = " + id + ", name = " + name);
        }

    }

}

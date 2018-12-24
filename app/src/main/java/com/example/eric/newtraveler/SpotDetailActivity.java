package com.example.eric.newtraveler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class SpotDetailActivity extends AppCompatActivity {

    private String mLongitude;
    private String mLatitude;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);
        Log.v(MainActivity.TAG, "SpotDetailActivity, onCreate ");

        Bundle bundle = getIntent().getExtras();
        loadInitCommonView(bundle);
    }

    public void loadInitCommonView(Bundle bundle) {

        if (bundle != null) {
            String id = bundle.getString("id");
            mName = bundle.getString("name");
            String category = bundle.getString("category");
            String address = bundle.getString("address");
            String telephone = bundle.getString("telephone");
            String content = bundle.getString("content");
            mLongitude = bundle.getString("longitude");
            mLatitude = bundle.getString("latitude");

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

}

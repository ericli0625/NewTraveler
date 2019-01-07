package com.example.eric.newtraveler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SpotDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String mLongitude;
    private String mLatitude;
    private String mName;

    private MapView mMapView;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);
        Log.v(MainActivity.TAG, "SpotDetailActivity, onCreate ");

        mMapView = (MapView) this.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        loadInitCommonView(bundle);
    }

    public void loadInitCommonView(Bundle bundle) {

        ImageButton returnIcon = (ImageButton) findViewById(R.id.return_icon);
        returnIcon.setOnClickListener(mButtonListener);

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

    public View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setContentView(R.layout.activity_main);
            onBackPressed();
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        LatLng location = new LatLng(Double.valueOf(mLatitude), Double.valueOf(mLongitude));
        mGoogleMap.addMarker(new MarkerOptions().position(location).title(mName));
        mGoogleMap.setMinZoomPreference(15.0f);
        mGoogleMap.setMaxZoomPreference(18.0f);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}

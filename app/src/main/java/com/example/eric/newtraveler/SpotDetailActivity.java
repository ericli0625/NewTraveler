package com.example.eric.newtraveler;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eric.newtraveler.database.SQLiteManager;
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
    private String mContent;
    private String mCategory;
    private String mAddress;
    private String mTelephone;

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private SQLiteManager mSQLiteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);
        Log.v(MainActivity.TAG, "SpotDetailActivity, onCreate ");

        mMapView = (MapView) this.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        mSQLiteManager = SQLiteManager.getInstance();

        Bundle bundle = getIntent().getExtras();
        loadInitCommonView(bundle);
    }

    public void loadInitCommonView(Bundle bundle) {

        ImageButton returnIcon = (ImageButton) findViewById(R.id.return_icon);
        returnIcon.setOnClickListener(mReturnButtonListener);

        ImageButton addFavoriteButton = (ImageButton) findViewById(R.id.add_favorite);
        addFavoriteButton.setOnClickListener(mAddFavoriteButtonListener);

        if (bundle != null) {
            String id = bundle.getString("id");
            mName = bundle.getString("name");
            mCategory = bundle.getString("category");
            mAddress = bundle.getString("address");
            mTelephone = bundle.getString("telephone");
            mContent = bundle.getString("content");
            mLongitude = bundle.getString("longitude");
            mLatitude = bundle.getString("latitude");

            TextView textViewNameDetail = (TextView) findViewById(R.id.textView_name_detail);
            TextView textViewCategoryDetail = (TextView) findViewById(R.id.textView_category_detail);
            TextView textViewAddressDetail = (TextView) findViewById(R.id.textView_address_detail);
            TextView textViewTelephoneDetail = (TextView) findViewById(R.id.textView_telephone_detail);
            TextView textViewContentDetail = (TextView) findViewById(R.id.textView_content_detail);

            textViewNameDetail.setText(mName);
            textViewCategoryDetail.setText(mCategory);
            textViewAddressDetail.setText(mAddress);
            textViewTelephoneDetail.setText(mTelephone);
            textViewContentDetail.setText(mContent);

        }

    }

    public View.OnClickListener mReturnButtonListener = v -> {
        setContentView(R.layout.activity_main);
        onBackPressed();
    };

    public View.OnClickListener mAddFavoriteButtonListener = v -> {
        // Perform action on click
        Cursor cursor = mSQLiteManager.matchData(mName, mCategory, mAddress, mTelephone,
                mLongitude, mLatitude, mContent);

        int rows_num = cursor.getCount();

        if (rows_num == 1) {
            Toast.makeText(v.getContext(), R.string.already_add_favorite_toast,
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            mSQLiteManager.insert(mName, mCategory, mAddress, mTelephone, mLongitude, mLatitude,
                    mContent);
            Toast.makeText(v.getContext(), R.string.add_favorite_toast, Toast.LENGTH_SHORT)
                    .show();
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

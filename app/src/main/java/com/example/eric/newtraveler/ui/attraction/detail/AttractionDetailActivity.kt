package com.example.eric.newtraveler.ui.attraction.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.ui.base.BaseActivity
import com.example.eric.newtraveler.util.SQLiteManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_attraction_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class AttractionDetailActivity : BaseActivity<AttractionDetailViewModel>(), OnMapReadyCallback {

    override val layoutRes: Int = R.layout.activity_attraction_detail
    override val viewModel: AttractionDetailViewModel by viewModel()

    private lateinit var longitude: String
    private lateinit var latitude: String
    private lateinit var name: String
    private lateinit var content: String
    private lateinit var category: String
    private lateinit var address: String
    private lateinit var telephone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val attraction = intent.extras?.getParcelable("attraction")
                ?: AttractionDetail.defaultInstance
        initLayout(attraction)

        mapView.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)
    }

    private fun initLayout(attraction: AttractionDetail) {
        name = attraction.name
        category = attraction.category
        address = attraction.address
        telephone = attraction.telephone
        content = attraction.content
        longitude = attraction.longitude
        latitude = attraction.latitude

        textView_category_detail.text = category
        textView_address_detail.text = address
        textView_telephone_detail.text = telephone
        textView_content_detail.text = content

        with(topAppBar) {
            title = name
            setNavigationOnClickListener { onBackPressed() }
        }
    }

    var mReturnButtonListener = View.OnClickListener { v: View? ->
        setContentView(R.layout.activity_main)
        onBackPressed()
    }
    var mAddFavoriteButtonListener = View.OnClickListener { v: View ->
        val cursor = SQLiteManager.getInstance()
                .matchData(name, category, address, telephone,
                        longitude, latitude, content)
        val rows_num = cursor.count
        cursor.close()
        if (rows_num == 1) {
            Toast.makeText(v.context, R.string.already_add_favorite_toast,
                    Toast.LENGTH_SHORT)
                    .show()
        } else {
            SQLiteManager.getInstance()
                    .insert(name, category, address, telephone, longitude, latitude,
                            content)
            Toast.makeText(v.context, R.string.add_favorite_toast, Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap ?: return
        val location = LatLng(latitude.toDouble(), longitude.toDouble())
        with(googleMap) {
            addMarker(MarkerOptions().position(location).title(name))
            moveCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_LEVEL))
        }
    }

    companion object {
        const val ZOOM_LEVEL = 13f

        fun launch(context: Context, attraction: AttractionDetail) {
            val intent = Intent().apply {
                setClass(context, AttractionDetailActivity::class.java)
                putExtra("attraction", attraction)
            }
            context.startActivity(intent)
        }
    }
}
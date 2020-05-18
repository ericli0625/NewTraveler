package com.example.eric.newtraveler.ui.attraction.detail

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.extension.hide
import com.example.eric.newtraveler.network.response.AttractionDetail
import com.example.eric.newtraveler.ui.base.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_attraction_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

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

        map_view.onCreate(savedInstanceState)
        map_view?.getMapAsync(this)
    }

    private fun initLayout(attraction: AttractionDetail) {
        name = attraction.name
        category = attraction.category
        address = attraction.address
        telephone = attraction.telephone
        content = attraction.content
        longitude = attraction.longitude
        latitude = attraction.latitude

        viewModel.showOrHideFavorIcon(name)

        if (category.isBlank()) {
            text_category.hide()
            text_category_detail.hide()
        } else {
            text_category_detail.text = category
        }

        if (address.isBlank()) {
            text_address.hide()
            text_address_detail.hide()
        } else {
            text_address_detail.text = address
        }

        if (telephone.isBlank()) {
            text_telephone.hide()
            text_telephone_detail.hide()
        } else {
            text_telephone_detail.text = telephone
        }

        if (content.isBlank()) {
            text_content.hide()
            text_content_detail.hide()
        } else {
            text_content_detail.text = content
        }

        with(toolbar) {
            title = name
            setNavigationOnClickListener { onBackPressed() }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_favorite -> {
                        if (checkMenuAction(it.icon)) {
                            viewModel.addFavorite(attraction)
                        } else {
                            viewModel.deleteFavorite(attraction)
                        }
                    }
                }
                true
            }
        }
    }

    private fun checkMenuAction(icon: Drawable): Boolean {
        val favorite = ContextCompat.getDrawable(applicationContext, R.drawable.ic_favorite_border)
        return Objects.equals(icon.constantState, favorite?.constantState)
    }

    override fun subscribeObservers() {
        super.subscribeObservers()
        subscribeToShowOrHideFavorIcon()
    }

    override fun onResume() {
        super.onResume()
        map_view.onResume()
    }

    override fun onPause() {
        map_view.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        map_view.onDestroy()
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

    /***** Subscribe methods implementation *****/

    private fun subscribeToShowOrHideFavorIcon() {
        viewModel.showOrHideFavorIcon.observe(this) {
            if (it.peekContent()) {
                with(toolbar.menu) {
                    findItem(R.id.action_favorite).icon =
                            ContextCompat.getDrawable(applicationContext, R.drawable.ic_favorite)
                }
            } else {
                with(toolbar.menu) {
                    findItem(R.id.action_favorite).icon =
                            ContextCompat.getDrawable(applicationContext,
                                    R.drawable.ic_favorite_border)
                }
            }
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
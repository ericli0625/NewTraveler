package com.example.eric.newtraveler.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.ui.adapter.*
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        view_pager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabs, view_pager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()
    }

    private fun getTabIcon(position: Int): Int {
        return when (position) {
            HOME_PAGE_INDEX -> R.drawable.ic_house
            SEARCH_PAGE_INDEX -> R.drawable.ic_search
            WEATHER_PAGE_INDEX -> R.drawable.ic_cloudy
            FAVOR_PAGE_INDEX -> R.drawable.ic_star
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            HOME_PAGE_INDEX -> getString(R.string.title_home)
            SEARCH_PAGE_INDEX -> getString(R.string.title_search)
            WEATHER_PAGE_INDEX -> getString(R.string.title_weather)
            FAVOR_PAGE_INDEX -> getString(R.string.title_favor)
            else -> null
        }
    }

    companion object {
        const val TAG = "Travel"
    }
}
package com.example.eric.newtraveler.ui

import android.os.Bundle
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.ui.adapter.*
import com.example.eric.newtraveler.ui.base.BaseActivity
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel>() {

    override val layoutRes: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
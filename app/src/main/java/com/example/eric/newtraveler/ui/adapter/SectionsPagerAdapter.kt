package com.example.eric.newtraveler.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eric.newtraveler.ui.favor.FavorFragment
import com.example.eric.newtraveler.ui.MainActivity
import com.example.eric.newtraveler.ui.search.SearchFragment
import com.example.eric.newtraveler.ui.home.HomeFragment
import com.example.eric.newtraveler.ui.weather.WeatherFragment

const val HOME_PAGE_INDEX = 0
const val SEARCH_PAGE_INDEX = 1
const val WEATHER_PAGE_INDEX = 2
const val FAVOR_PAGE_INDEX = 3

class SectionsPagerAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {

    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
            HOME_PAGE_INDEX to { HomeFragment.newInstance() },
            SEARCH_PAGE_INDEX to { SearchFragment.newInstance() },
            WEATHER_PAGE_INDEX to { WeatherFragment.newInstance() },
            FAVOR_PAGE_INDEX to { FavorFragment.newInstance() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int) =
            tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()

}
package com.example.eric.newtraveler.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.example.eric.newtraveler.R
import com.example.eric.newtraveler.ui.adapter.*
import com.example.eric.newtraveler.ui.base.BaseActivity
import com.example.eric.newtraveler.ui.search.SearchFragment
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel>() {

    override val layoutRes: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate()

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        view_pager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabs, view_pager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModel.fetchAndActivateRemoteConfig()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.top_bar_search, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as (SearchView)
        searchView.setOnQueryTextListener(onQueryTextListener)
        return super.onCreateOptionsMenu(menu)
    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            view_pager.currentItem = 1
            GlobalScope.launch {
                delay(100)
                supportFragmentManager.fragments.forEach {
                    if (it is SearchFragment) {
                        it.searchAttraction(query)
                    }
                }
            }
            return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
            return false
        }
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
}
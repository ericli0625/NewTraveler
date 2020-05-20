package com.example.eric.newtraveler

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.eric.newtraveler.ui.home.HomeRepository
import com.example.eric.newtraveler.ui.home.HomeViewModel
import com.example.eric.newtraveler.util.RxImmediateSchedulerRule
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var repository: HomeRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun testShowCountyList() {

    }
}
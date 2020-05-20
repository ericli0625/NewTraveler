package com.example.eric.newtraveler

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.eric.newtraveler.ui.favor.FavorRepository
import com.example.eric.newtraveler.ui.favor.FavorViewModel
import com.example.eric.newtraveler.util.FakeData
import com.example.eric.newtraveler.util.RxImmediateSchedulerRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test

class FavorViewModelTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var repository: FavorRepository

    private lateinit var viewModel: FavorViewModel

    private val fakeAttractions = FakeData.getFakeAttractions()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = FavorViewModel(repository)
    }

    @Test
    fun testGetAllAttractions() {

        every { repository.getAllAttractions() } returns Observable.just(fakeAttractions)

        viewModel.getAllAttractions()

        assertEquals(
                fakeAttractions,
                viewModel.showAttractionListEvent.value?.peekContent()
        )
    }
}
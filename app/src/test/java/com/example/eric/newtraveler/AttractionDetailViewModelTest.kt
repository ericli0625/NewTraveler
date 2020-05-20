package com.example.eric.newtraveler

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.eric.newtraveler.ui.attraction.detail.AttractionDetailRepository
import com.example.eric.newtraveler.ui.attraction.detail.AttractionDetailViewModel
import com.example.eric.newtraveler.util.FakeData
import com.example.eric.newtraveler.util.RxImmediateSchedulerRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.Assert.*

class AttractionDetailViewModelTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var repository: AttractionDetailRepository

    private lateinit var viewModel: AttractionDetailViewModel

    private val fakeAttraction = FakeData.getFakeAttraction()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = AttractionDetailViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testAddFavorite() {
        coEvery { repository.addFavorite(fakeAttraction) } returns Unit

        viewModel.addFavorite(fakeAttraction)

        assertEquals(
                R.string.snack_bar_add_msg,
                viewModel.showSnackBarEvent.value?.peekContent()
        )
    }

    @Test
    fun testDeleteFavorite() {
        coEvery { repository.deleteFavorite(fakeAttraction) } returns Unit

        viewModel.deleteFavorite(fakeAttraction)

        assertEquals(
                R.string.snack_bar_delete_msg,
                viewModel.showSnackBarEvent.value?.peekContent()
        )
    }

    @Test
    fun testShowFavorIcon() {

        every { repository.getAllAttractions() } returns
                Observable.just(listOf(fakeAttraction))

        viewModel.showOrHideFavorIcon("台北美術館")

        assertTrue(viewModel.showOrHideFavorIcon.value?.peekContent() ?: false)
    }

    @Test
    fun testHideFavorIcon() {

        every { repository.getAllAttractions() } returns
                Observable.just(listOf(fakeAttraction))

        viewModel.showOrHideFavorIcon("二二八紀念公園")

        assertFalse(viewModel.showOrHideFavorIcon.value?.peekContent() ?: true)
    }
}
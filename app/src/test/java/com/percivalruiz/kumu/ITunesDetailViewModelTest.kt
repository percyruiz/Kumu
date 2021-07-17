package com.percivalruiz.kumu

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.jraska.livedata.test
import com.percivalruiz.kumu.data.ITunesItem
import com.percivalruiz.kumu.data.UserStatus
import com.percivalruiz.kumu.repository.ITunesRepository
import com.percivalruiz.kumu.repository.UserStatusRepository
import com.percivalruiz.kumu.ui.detail.ITunesDetailViewModel
import com.percivalruiz.kumu.ui.detail.ITunesDetailViewModel.Companion.KEY_ITUNES_ID
import com.percivalruiz.kumu.ui.list.ITunesListViewModel
import com.percivalruiz.kumu.ui.list.ITunesListViewModel.Companion.KEY_TERM
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import io.mockk.just

@ExperimentalCoroutinesApi
class ITunesDetailViewModelTest {

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val dispatcher = TestCoroutineDispatcher()

  private lateinit var underTest: ITunesDetailViewModel

  @RelaxedMockK
  private lateinit var handle: SavedStateHandle
  @MockK
  private lateinit var iTunesRepository: ITunesRepository
  @MockK
  private lateinit var userStatusRepository: UserStatusRepository

  @Before
  fun setup() {
    MockKAnnotations.init(this)
    Dispatchers.setMain(dispatcher)
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    dispatcher.cleanupTestCoroutines()
  }

  @Test
  fun `should get itunes item`() {
    val item = ITunesItem(
      id = 1,
      trackName = "Loki",
      artworkUrl100 = "artworkUrl100",
      trackPrice = 1f,
      primaryGenreName = "Series",
      currency = "USD",
      longDescription = "Loki is an American television series created by Michael Waldron for the streaming service Disney+"
    )

    // mock handles

    every { handle.contains(KEY_ITUNES_ID)} returns false

    every {
      handle.getLiveData<Long>(KEY_ITUNES_ID)
    } returns MutableLiveData(1)

    // mock repositories

    coEvery { userStatusRepository.saveUserStatus("Loki")} just Runs

    coEvery { iTunesRepository.getItem(1) } returns flow {
      emit(item)
    }

    underTest = ITunesDetailViewModel(handle, iTunesRepository)
    underTest.getItem(1)

    // assert itunes list is fetched
    underTest.iTunesItem.asLiveData().test().assertValue(item)
  }
}
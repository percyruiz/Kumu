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
class ITunesListViewModelTest {

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  private val dispatcher = TestCoroutineDispatcher()

  private lateinit var underTest: ITunesListViewModel

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
    init()
  }

  @After
  fun tearDown() {
    Dispatchers.resetMain()
    dispatcher.cleanupTestCoroutines()
  }

  @Test
  fun `should get list of itunes items on init`() {
    underTest = ITunesListViewModel(handle, iTunesRepository, userStatusRepository, dispatcher)

    // assert itunes list is fetched
    underTest.iTunesItems.asLiveData().test()
      .assertHasValue()

    // assert status is saved
    underTest.status.test().assertHasValue()
  }

  @Test
  fun `should get list of itunes items on search`() {
    val items = listOf(
      createITunesItem(1),
      createITunesItem(2),
      createITunesItem(3),
    )

    val status = UserStatus(0, "Sylvie", 1)

    underTest = ITunesListViewModel(handle, iTunesRepository, userStatusRepository, dispatcher)

    every {
      handle.get<String>(KEY_TERM)
    } returns "Sylvie"

    every { handle.contains(KEY_TERM) } returns true

    every {
      handle.getLiveData<String>(KEY_TERM)
    } returns MutableLiveData("Sylvie")

    // mock repositories

    coEvery { userStatusRepository.saveUserStatus("Sylvie") } just Runs

    coEvery {
      userStatusRepository.getUserStatus()
    } returns status

    coEvery { iTunesRepository.search("Sylvie") } returns flow {
      emit(PagingData.from(items))
    }

    underTest.search("Sylvie")

    // assert itunes list is fetched
    underTest.iTunesItems.asLiveData().test()
      .assertHasValue()

    // assert status is saved
    underTest.status.test().assertValue(status)
  }

  private fun init() {
    val items = listOf(
      createITunesItem(1),
      createITunesItem(2),
      createITunesItem(3),
    )

    every {
      handle.get<String>(KEY_TERM)
    } returns "Loki"

    every { handle.contains(KEY_TERM)} returns true

    every {
      handle.getLiveData<String>(KEY_TERM)
    } returns MutableLiveData("Loki")

    // mock repositories

    coEvery { userStatusRepository.saveUserStatus("Loki")} just Runs

    coEvery {
      userStatusRepository.getUserStatus()
    } returns null

    coEvery { iTunesRepository.search("Loki") } returns flow {
      emit(PagingData.from(items))
    }
  }

  companion object {
    fun createITunesItem(id: Long) = ITunesItem(
      id = id,
      trackName = "",
      artworkUrl100 = "",
      trackPrice = 1f,
      primaryGenreName = "",
      currency = "",
      longDescription = ""
    )
  }
}
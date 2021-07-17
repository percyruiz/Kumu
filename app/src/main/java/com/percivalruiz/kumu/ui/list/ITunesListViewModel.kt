package com.percivalruiz.kumu.ui.list

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.percivalruiz.kumu.data.UserStatus
import com.percivalruiz.kumu.repository.ITunesRepository
import com.percivalruiz.kumu.repository.UserStatusRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Contains logic for getting iTunes list items and saving user status
 *
 * @property handle implements caching of values binded on the viewmodel's lifecycle
 * @property iTunesRepository data source for [ITunesItem] related data
 * @property userStatusRepository data source for [UserStatus]related data
 * @property dispatcher enables injection of coroutine dispatcher to the viewmodel
 */
class ITunesListViewModel(
  private val handle: SavedStateHandle,
  private val iTunesRepository: ITunesRepository,
  private val userStatusRepository: UserStatusRepository,
  private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

  private val _status = MutableLiveData<UserStatus?>()
  val status: LiveData<UserStatus?> = _status

  init {
    viewModelScope.launch(dispatcher) {

      // Handles configuration change
      if (handle.contains(KEY_TERM)) {
        saveUserStatus()
      }

      // initialize to a search term when nothing is saved in db cache and savedstatehandle
      if(userStatusRepository.getUserStatus() == null && !handle.contains(KEY_TERM)) {
        saveUserStatus()
      }

      _status.postValue(userStatusRepository.getUserStatus())
    }
  }

  /**
   * This will set the last search value from db
   */
  fun initStatus(status: UserStatus?) {
    if (!handle.contains(KEY_TERM)) {
      handle.set(KEY_TERM, status?.lastSearch.orEmpty())
    }
  }

  /**
   * Observe changes from the handle as a [LiveData] object
   * This call iTunes service's search method on [LiveData] updates
   */
  @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
  val iTunesItems = handle.getLiveData<String>(KEY_TERM)
    .asFlow()
    .flatMapLatest { iTunesRepository.search(it) }
    .cachedIn(viewModelScope)

  /**
   * Handles searching by updating the handle then saving user status
   *
   * @param term is the user's search input
   */
  fun search(term: String) {
    handle.set(KEY_TERM, term)
    viewModelScope.launch(dispatcher) {
      saveUserStatus()
      _status.postValue(userStatusRepository.getUserStatus())
    }
  }

  private suspend fun saveUserStatus(default: String = "Loki") {
    userStatusRepository.saveUserStatus(handle.get<String>(KEY_TERM) ?: default)
  }

  companion object {
    const val KEY_TERM = "com.percivalruiz.kumu.keyTerm"
  }
}
package com.percivalruiz.kumu.ui.list

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.percivalruiz.kumu.data.UserStatus
import com.percivalruiz.kumu.repository.ITunesRepository
import com.percivalruiz.kumu.repository.UserStatusRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flatMapLatest

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

  fun initStatus(status: UserStatus?) {
    if (!handle.contains(KEY_TERM)) {
      handle.set(KEY_TERM, status?.lastSearch.orEmpty())
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
  val iTunesItems = handle.getLiveData<String>(KEY_TERM)
    .asFlow()
    .flatMapLatest { iTunesRepository.search(it) }
    .cachedIn(viewModelScope)

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
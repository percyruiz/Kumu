package com.percivalruiz.kumu.ui

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.percivalruiz.kumu.data.UserStatus
import com.percivalruiz.kumu.repository.ITunesRepository
import com.percivalruiz.kumu.repository.UserStatusRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ITunesListViewModel(
  private val handle: SavedStateHandle,
  private val iTunesRepository: ITunesRepository,
  private val userStatusRepository: UserStatusRepository
) : ViewModel() {

  private val _status = MutableLiveData<UserStatus?>()
  val status: LiveData<UserStatus?> = _status

  init {
    viewModelScope.launch(Dispatchers.IO) {
      if (handle.contains(KEY_TERM)) {
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
    viewModelScope.launch(Dispatchers.IO) {
      if (handle.contains(KEY_TERM)) {
        saveUserStatus()
      }
      _status.postValue(userStatusRepository.getUserStatus())
    }
  }

  private suspend fun saveUserStatus() {
    userStatusRepository.saveUserStatus(handle.get<String>(KEY_TERM).orEmpty())
  }

  companion object {
    const val KEY_TERM = "com.percivalruiz.kumu.keyTerm"
  }
}
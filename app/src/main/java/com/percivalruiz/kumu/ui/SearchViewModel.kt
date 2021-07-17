package com.percivalruiz.kumu.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.percivalruiz.kumu.repository.ITunesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest

class SearchViewModel(
  private val handle: SavedStateHandle,
  private val repository: ITunesRepository
): ViewModel() {

  init {
    if (!handle.contains(KEY_TERM)) {
      handle.set(KEY_TERM, "")
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
  val iTunesItems = handle.getLiveData<String>(KEY_TERM)
    .asFlow()
    .flatMapLatest { repository.search(it) }
    .cachedIn(viewModelScope)

//  suspend fun search(term: String): Flow<List<ITunesItem>> {
//    return repository.search(term)
//  }

  fun search(term: String) {
    handle.set(KEY_TERM, term)
  }

  companion object {
    const val KEY_TERM = "com.percivalruiz.kumu.keyTerm"
  }
}
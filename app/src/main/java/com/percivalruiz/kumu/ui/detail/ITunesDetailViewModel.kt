package com.percivalruiz.kumu.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.percivalruiz.kumu.repository.ITunesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest

class ITunesDetailViewModel(
  private val handle: SavedStateHandle,
  private val repository: ITunesRepository
): ViewModel() {

  init {
    if (!handle.contains(KEY_ITUNES_ID)) {
      handle.set(KEY_ITUNES_ID, 0)
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
  val iTunesItem = handle.getLiveData<Long>(KEY_ITUNES_ID)
    .asFlow()
    .flatMapLatest { repository.getItem(it) }

  fun getItem(id: Long) {
    handle.set(KEY_ITUNES_ID, id)
  }

  companion object {
    const val KEY_ITUNES_ID = "com.percivalruiz.kumu.iTunesId"
  }
}
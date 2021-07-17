package com.percivalruiz.kumu.ui.detail

import androidx.lifecycle.*
import com.percivalruiz.kumu.repository.ITunesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flatMapLatest

/**
 * Contains logic for getting iTunes list items and saving user status
 *
 * @property handle implements caching of values binded on the viewmodel's lifecycle
 * @property repository data source for [ITunesItem] related data
 */
class ITunesDetailViewModel(
  private val handle: SavedStateHandle,
  private val repository: ITunesRepository
): ViewModel() {

  init {
    if (!handle.contains(KEY_ITUNES_ID)) {
      handle.set(KEY_ITUNES_ID, 0)
    }
  }

  /**
   * Observe changes from the handle as a [LiveData] object
   * This call iTunes service's search method on [LiveData] updates
   */
  @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
  val iTunesItem = handle.getLiveData<Long>(KEY_ITUNES_ID)
    .asFlow()
    .flatMapLatest { repository.getItem(it) }

  /**
   * Handles getting iTunes item by updating the handle
   *
   * @param id is the item's id on the db
   */
  fun getItem(id: Long) {
    handle.set(KEY_ITUNES_ID, id)
  }

  companion object {
    const val KEY_ITUNES_ID = "com.percivalruiz.kumu.iTunesId"
  }
}
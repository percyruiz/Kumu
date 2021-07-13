package com.percivalruiz.kumu.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.percivalruiz.kumu.data.Repository
import com.percivalruiz.kumu.data.Results
import kotlinx.coroutines.flow.Flow

class SearchViewModel(
  private val handle: SavedStateHandle,
  private val repository: Repository
): ViewModel() {

  init {
    if (!handle.contains(KEY_TERM)) {
      handle.set(KEY_TERM, "")
    }
  }

  suspend fun search(term: String): Flow<List<Results>> {
    return repository.search(term)
  }

  companion object {
    const val KEY_TERM = "com.percivalruiz.kumu.keyTerm"
  }
}
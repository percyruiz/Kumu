package com.percivalruiz.kumu.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class SearchViewModel(
  private val handle: SavedStateHandle
): ViewModel() {

  init {
    if (!handle.contains(KEY_TERM)) {
      handle.set(KEY_TERM, "")
    }
  }

  fun search(term: String) {

  }

  companion object {
    const val KEY_TERM = "com.percivalruiz.kumu.keyTerm"
  }
}
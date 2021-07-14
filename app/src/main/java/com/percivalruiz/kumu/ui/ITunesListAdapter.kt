package com.percivalruiz.kumu.ui

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.RequestManager
import com.percivalruiz.kumu.data.ITunesItem

class ITunesListAdapter(
  private val glide: RequestManager,
  private val onClick: () -> Unit
) : PagingDataAdapter<ITunesItem, ITunesViewHolder>(ITEM_COMP) {

  override fun onBindViewHolder(holder: ITunesViewHolder, position: Int) {
    holder.bind(getItem(position))
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ITunesViewHolder {
    return ITunesViewHolder.create(parent, onClick, glide)
  }

  companion object {
    private val ITEM_COMP = object : DiffUtil.ItemCallback<ITunesItem>() {
      override fun areItemsTheSame(oldItem: ITunesItem, newItem: ITunesItem): Boolean {
        return oldItem.id == newItem.id
      }

      override fun areContentsTheSame(oldItem: ITunesItem, newItem: ITunesItem): Boolean =
        oldItem == newItem
    }
  }
}
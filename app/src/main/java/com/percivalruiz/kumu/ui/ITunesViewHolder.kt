package com.percivalruiz.kumu.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.percivalruiz.kumu.R
import com.percivalruiz.kumu.data.ITunesItem

class ITunesViewHolder(
  view: View,
  onClick: () -> Unit,
  private val glide: RequestManager
) : RecyclerView.ViewHolder(view) {

  private val trackImage: ImageView = view.findViewById(R.id.track_image)
  private val trackName: TextView = view.findViewById(R.id.track_name)
  private val trackPrice: TextView = view.findViewById(R.id.track_price)

  init {
    view.setOnClickListener {
      onClick.invoke()
    }
  }

  fun bind(item: ITunesItem?) {
    item?.apply {
      this@ITunesViewHolder.trackName.text = item.trackName
      this@ITunesViewHolder.trackPrice.text = if (item.trackPrice != null) {
        "${item.trackPrice} ${item.currency}"
      } else {
        "--"
      }
      glide.load(item.artworkUrl100)
        .centerCrop()
        .transform(CenterCrop(), RoundedCorners(5))
        .placeholder(R.drawable.ic_baseline_music_note_24)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this@ITunesViewHolder.trackImage)

    }
  }

  companion object {
    fun create(parent: ViewGroup, onClick: () -> Unit, glide: RequestManager): ITunesViewHolder {
      val view = LayoutInflater.from(parent.context).inflate(R.layout.itunes_item, parent, false)
      return ITunesViewHolder(view, onClick, glide)
    }
  }
}
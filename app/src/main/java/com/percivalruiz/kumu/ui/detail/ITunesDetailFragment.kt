package com.percivalruiz.kumu.ui.detail

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.percivalruiz.kumu.R
import com.percivalruiz.kumu.databinding.FragmentItunesDetailBinding
import com.percivalruiz.kumu.ui.list.ITunesListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class ITunesDetailFragment : Fragment() {

  private val viewModel: ITunesDetailViewModel by stateViewModel()
  private val args: ITunesDetailFragmentArgs by navArgs()
  private val glide: RequestManager by inject()

  private var _binding: FragmentItunesDetailBinding? = null
  private val binding
    get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentItunesDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.getItem(args.id)

      viewModel.iTunesItem.collectLatest {
        it?.run {
          requireActivity().toolbar.title = trackName
          if (trackPrice != null) {
            binding.trackPriceDetail.text = "$trackPrice $currency"
          }
          binding.trackGenreDetail.text = primaryGenreName
          longDescription?.let { desc ->
            binding.trackDescriptionDetail.text =
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(desc, Html.FROM_HTML_MODE_COMPACT)
              } else {
                Html.fromHtml(desc)
              }
          }

          glide.load(artworkUrl100)
            .centerInside()
            .placeholder(R.drawable.ic_baseline_music_note_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.trackImageDetail)
        }
      }
    }
  }
}
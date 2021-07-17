package com.percivalruiz.kumu.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.percivalruiz.kumu.R
import com.percivalruiz.kumu.databinding.FragmentItunesListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.stateViewModel
import org.ocpsoft.prettytime.PrettyTime
import java.util.*

class ITunesListFragment : Fragment() {

  private val viewModel: ITunesListViewModel by stateViewModel()
  private val glide: RequestManager by inject()
  private lateinit var adapter: ITunesListAdapter
  private var _binding: FragmentItunesListBinding? = null
  private val binding
    get() = _binding!!

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentItunesListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.retryButton.setOnClickListener {
      findNavController().navigate(ITunesListFragmentDirections.actionListToDetail())
    }

    initAdapter()

    binding.searchTerm.setOnEditorActionListener { v, actionId, event ->
      if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        viewModel.search(binding.searchTerm.text.toString())
        hideKeyboard()
        true
      }
      false
    }

    viewModel.status.observe(viewLifecycleOwner) {
      viewModel.initStatus(status = it)
      binding.searchTerm.setText(it?.lastSearch.orEmpty())
      it?.lastVisit?.let { timestamp ->
        binding.lastVisitLabel.text =
          requireContext().resources.getString(R.string.last_visited, timestamp.getPrettyTime())
      }
    }
  }

  private fun Long.getPrettyTime(): String {
    return PrettyTime().format(Date(this))
  }

  private fun initAdapter() {
    adapter = ITunesListAdapter(glide) { }

    binding.retryButton.setOnClickListener { adapter.retry() }

    val footerAdapter = LoadingAdapter(adapter)
    binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
      header = LoadingAdapter(adapter),
      footer = footerAdapter
    )

    val gridLayoutManager = GridLayoutManager(activity, 3)
    binding.list.layoutManager = gridLayoutManager
    gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
      override fun getSpanSize(position: Int): Int {
        return if (position == adapter.itemCount && footerAdapter.itemCount > 0) {
          3
        } else {
          1
        }
      }
    }

    viewLifecycleOwner.lifecycleScope.launchWhenCreated {
      viewModel.iTunesItems.collectLatest {
        adapter.submitData(it)
      }
    }

    adapter.addLoadStateListener { loadState ->
      // show empty list
      val isListEmpty = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
      showEmptyList(isListEmpty)
    }

    viewLifecycleOwner.lifecycleScope.launch {
      adapter.loadStateFlow.collectLatest { loadStates ->
        binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
        binding.retryButton.isVisible =
          loadStates.refresh !is LoadState.Loading && adapter.itemCount == 0 && loadStates.refresh is LoadState.Error

        if (loadStates.refresh !is LoadState.Loading && loadStates.refresh is LoadState.Error) {
          Toast.makeText(
            requireContext(),
            (loadStates.refresh as? LoadState.Error)?.error?.message,
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }
  }

  private fun showEmptyList(show: Boolean) {
    if (show) {
      binding.emptyList.visibility = View.VISIBLE
      binding.list.visibility = View.GONE
    } else {
      binding.emptyList.visibility = View.GONE
      binding.list.visibility = View.VISIBLE
    }
  }

  private fun hideKeyboard(): Boolean {
    return (requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
      .hideSoftInputFromWindow(binding.searchTerm.windowToken, 0)
  }
}

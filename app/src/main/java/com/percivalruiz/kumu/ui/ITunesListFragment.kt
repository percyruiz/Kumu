package com.percivalruiz.kumu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.percivalruiz.kumu.databinding.FragmentItunesListBinding

class ITunesListFragment: Fragment() {

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
  }
}

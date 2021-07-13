package com.percivalruiz.kumu.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.percivalruiz.kumu.databinding.FragmentItunesDetailBinding

class ITunesDetailFragment: Fragment() {

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

  }
}
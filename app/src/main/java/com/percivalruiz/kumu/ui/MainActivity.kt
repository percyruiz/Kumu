package com.percivalruiz.kumu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.percivalruiz.kumu.R
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MainActivity : AppCompatActivity() {

  private val viewModel: SearchViewModel by stateViewModel()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }
}
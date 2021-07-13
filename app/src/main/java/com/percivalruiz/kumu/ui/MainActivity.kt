package com.percivalruiz.kumu.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.percivalruiz.kumu.R
import com.percivalruiz.kumu.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.stateViewModel

class MainActivity : AppCompatActivity() {

  private val viewModel: SearchViewModel by stateViewModel()
  private lateinit var binding: ActivityMainBinding
  private lateinit var appBarConfig: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    navHost.navController.apply {
      appBarConfig = AppBarConfiguration(graph)

      addOnDestinationChangedListener { _, destination, _ ->
        when(destination.id) {
          R.id.iTunesListFragment -> binding.toolbar.isVisible = false
          R.id.iTunesDetailFragment -> binding.toolbar.isVisible = true
        }
      }
    }

    setSupportActionBar(binding.toolbar)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)

    lifecycleScope.launch {
      viewModel.search("rick and morty").collectLatest {
        Log.d("test", it[0].trackName)
      }
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfig)
  }
}

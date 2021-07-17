package com.percivalruiz.kumu.di

import android.util.Log
import androidx.room.Room
import com.bumptech.glide.Glide
import com.percivalruiz.kumu.api.ITunesService
import com.percivalruiz.kumu.repository.ITunesRepository
import com.percivalruiz.kumu.repository.ITunesRepositoryImpl
import com.percivalruiz.kumu.db.AppDatabase
import com.percivalruiz.kumu.repository.UserStatusRepository
import com.percivalruiz.kumu.repository.UserStatusRepositoryImpl
import com.percivalruiz.kumu.ui.ITunesListViewModel
import com.percivalruiz.kumu.ui.SearchViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Handles dependency injection using Koin
 */
val appModule = module {

  single {
    Moshi.Builder()
      .add(KotlinJsonAdapterFactory())
      .build()
  }

  single {
    val logger = HttpLoggingInterceptor { Log.d("API", it) }
    logger.level = HttpLoggingInterceptor.Level.BODY
    OkHttpClient.Builder()
      .addInterceptor(logger)
      .build()
  }

  single<ITunesService> {
    val retrofit = Retrofit.Builder()
      .baseUrl("https://itunes.apple.com/")
      .client(get())
      .addConverterFactory(MoshiConverterFactory.create(get()))
      .build()
    retrofit.create(ITunesService::class.java)
  }

  single {
    Room.databaseBuilder(
      androidApplication(),
      AppDatabase::class.java, "kumu"
    ).build()
  }

  single<ITunesRepository> {
    ITunesRepositoryImpl(
      service = get(),
      db = get()
    )
  }

  single<UserStatusRepository> {
    UserStatusRepositoryImpl(
      db = get()
    )
  }

  viewModel {
    SearchViewModel(
      handle = get(),
      repository = get()
    )
  }

  viewModel {
    ITunesListViewModel(
      handle = get(),
      iTunesRepository = get(),
      userStatusRepository = get()
    )
  }

  single {
    Glide.with(androidContext())
  }
}

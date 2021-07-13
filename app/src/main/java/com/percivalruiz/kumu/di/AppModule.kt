package com.percivalruiz.kumu.di

import android.util.Log
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

  single {
    Retrofit.Builder()
      .baseUrl("https://itunes.apple.com/")
      .client(get())
      .addConverterFactory(MoshiConverterFactory.create(get()))
      .build()
  }

}

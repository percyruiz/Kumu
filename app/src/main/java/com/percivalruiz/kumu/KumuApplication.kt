package com.percivalruiz.kumu

import android.app.Application
import com.percivalruiz.kumu.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class KumuApplication: Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@KumuApplication)
      modules(appModule)
    }
  }
}
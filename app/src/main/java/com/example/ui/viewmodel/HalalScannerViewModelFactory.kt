package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.local.createUserPreferences
import com.example.data.local.getAppDatabase
import com.example.data.repository.ProductRepository
import com.example.data.repository.androidIsOnline

// Android tarafı: veritabanı, tercihler ve bağlantı kontrolü burada bağlanır; ViewModel ortak kodda kalır.
class HalalScannerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repository = ProductRepository(
            getAppDatabase(application).productDao(),
            isOnlineCheck = { androidIsOnline(application) }
        )
        return HalalScannerViewModel(repository, createUserPreferences(application)) as T
    }
}

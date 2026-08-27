package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AppLanguage
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HalalScannerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository

    private val _selectedLanguage = MutableStateFlow(AppLanguage.EN)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    private val _isScannerOpen = MutableStateFlow(false)
    val isScannerOpen: StateFlow<Boolean> = _isScannerOpen.asStateFlow()

    private val _activeProduct = MutableStateFlow<FoodProduct?>(null)
    val activeProduct: StateFlow<FoodProduct?> = _activeProduct.asStateFlow()

    private val _isManualEntryOpen = MutableStateFlow(false)
    val isManualEntryOpen: StateFlow<Boolean> = _isManualEntryOpen.asStateFlow()

    private val _isEAdditivesOpen = MutableStateFlow(false)
    val isEAdditivesOpen: StateFlow<Boolean> = _isEAdditivesOpen.asStateFlow()

    private val _isLanguageDialogOpen = MutableStateFlow(false)
    val isLanguageDialogOpen: StateFlow<Boolean> = _isLanguageDialogOpen.asStateFlow()

    private val _selectedFilter = MutableStateFlow<HalalStatus?>(null)
    val selectedFilter: StateFlow<HalalStatus?> = _selectedFilter.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var scanJob: Job? = null

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = ProductRepository(database.productDao())
        viewModelScope.launch {
            repository.ensureDatabaseSeeded()
        }
    }

    val scanHistory: StateFlow<List<FoodProduct>> = repository.getScanHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setLanguage(language: AppLanguage) {
        _selectedLanguage.value = language
    }

    fun openLanguageDialog() {
        _isLanguageDialogOpen.value = true
    }

    fun closeLanguageDialog() {
        _isLanguageDialogOpen.value = false
    }

    fun openScanner() {
        _isScannerOpen.value = true
    }

    fun closeScanner() {
        _isScannerOpen.value = false
    }

    fun openManualEntry() {
        _isManualEntryOpen.value = true
    }

    fun closeManualEntry() {
        _isManualEntryOpen.value = false
    }

    fun openEAdditives() {
        _isEAdditivesOpen.value = true
    }

    fun closeEAdditives() {
        _isEAdditivesOpen.value = false
    }

    fun dismissResult() {
        _activeProduct.value = null
    }

    fun setFilter(status: HalalStatus?) {
        _selectedFilter.value = status
    }

    fun cancelScan() {
        scanJob?.cancel()
        _isLoading.value = false
    }

    fun onBarcodeScanned(barcode: String) {
        scanJob = viewModelScope.launch {
            _isLoading.value = true
            _isScannerOpen.value = false
            try {
                val product = repository.checkBarcode(barcode, _selectedLanguage.value)
                _activeProduct.value = product
            } catch (e: Exception) {
                Log.e("HalalScannerViewModel", "checkBarcode failed for '$barcode'", e)
                _activeProduct.value = FoodProduct(
                    barcode = barcode,
                    name = "Error",
                    brand = "",
                    category = "Global Food",
                    status = HalalStatus.BULUNAMADI,
                    halalCertificate = null,
                    harmfulOrSuspiciousIngredients = emptyList(),
                    allIngredients = emptyList(),
                    reasonOrDetails = "Beklenmeyen bir hata oluştu: ${e.message}. Lütfen tekrar deneyin.",
                    alternatives = emptyList(),
                    imageUrl = null
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectHistoryItem(product: FoodProduct) {
        _activeProduct.value = product
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearScanHistory()
        }
    }
}

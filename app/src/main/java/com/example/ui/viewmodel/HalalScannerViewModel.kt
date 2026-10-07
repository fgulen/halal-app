package com.example.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.UserPreferences
import com.example.data.model.AppLanguage
import com.example.data.model.AppStrings
import com.example.data.model.FoodProduct
import com.example.data.model.HalalStatus
import com.example.data.model.Madhhab
import com.example.data.repository.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class ScanError {
    data class NetworkUnavailable(val query: String) : ScanError()
    data class Unexpected(val message: String) : ScanError()
}

class HalalScannerViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository
    private val userPreferences = UserPreferences(application)

    private val _selectedLanguage = MutableStateFlow(AppLanguage.EN)
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    // Loaded from prefs asynchronously in init, like language - a scan in the first instant after
    // launch can still run as UNSPECIFIED (harmless while no override exists; see the plan doc).
    private val _selectedMadhhab = MutableStateFlow(Madhhab.UNSPECIFIED)
    val selectedMadhhab: StateFlow<Madhhab> = _selectedMadhhab.asStateFlow()

    private val _isMadhhabDialogOpen = MutableStateFlow(false)
    val isMadhhabDialogOpen: StateFlow<Boolean> = _isMadhhabDialogOpen.asStateFlow()

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

    private val _scanError = MutableStateFlow<ScanError?>(null)
    val scanError: StateFlow<ScanError?> = _scanError.asStateFlow()

    private val _photoIngredientsReview = MutableStateFlow<String?>(null)
    val photoIngredientsReview: StateFlow<String?> = _photoIngredientsReview.asStateFlow()

    private var lastScanQuery: String? = null
    private var scanJob: Job? = null

    init {
        val database = AppDatabase.getDatabase(application)
        repository = ProductRepository(database.productDao(), appContext = application)
        viewModelScope.launch {
            _selectedLanguage.value = userPreferences.selectedLanguage.first()
        }
        viewModelScope.launch {
            _selectedMadhhab.value = userPreferences.selectedMadhhab.first()
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
        viewModelScope.launch {
            userPreferences.setSelectedLanguage(language)
        }
    }

    fun setMadhhab(madhhab: Madhhab) {
        _selectedMadhhab.value = madhhab
        viewModelScope.launch {
            userPreferences.setSelectedMadhhab(madhhab)
        }
    }

    fun openMadhhabDialog() {
        _isMadhhabDialogOpen.value = true
    }

    fun closeMadhhabDialog() {
        _isMadhhabDialogOpen.value = false
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
        lastScanQuery = barcode
        scanJob = viewModelScope.launch {
            _isLoading.value = true
            _isScannerOpen.value = false
            _scanError.value = null
            try {
                val product = repository.checkBarcode(barcode, _selectedLanguage.value, _selectedMadhhab.value)
                _activeProduct.value = product
            } catch (e: ProductRepository.NetworkUnavailableException) {
                Log.w("HalalScannerViewModel", "Network unavailable while checking '$barcode'")
                _scanError.value = ScanError.NetworkUnavailable(barcode)
            } catch (e: Exception) {
                Log.e("HalalScannerViewModel", "checkBarcode failed for '$barcode'", e)
                _scanError.value = ScanError.Unexpected(
                    AppStrings.getUnexpectedError(_selectedLanguage.value, e.message ?: AppStrings.getUnknownError(_selectedLanguage.value))
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun dismissScanError() {
        _scanError.value = null
    }

    fun retryLastScan() {
        val query = lastScanQuery ?: return
        _scanError.value = null
        onBarcodeScanned(query)
    }

    fun selectHistoryItem(product: FoodProduct) {
        _activeProduct.value = product
    }

    /** A photo was captured and OCR'd (possibly to an empty string if nothing was recognized). */
    fun onPhotoTextRecognized(ocrText: String) {
        _photoIngredientsReview.value = ocrText
    }

    fun dismissPhotoReview() {
        _photoIngredientsReview.value = null
    }

    fun analyzeIngredientsPhoto(productName: String, ingredientsText: String) {
        _photoIngredientsReview.value = null
        viewModelScope.launch {
            val product = repository.analyzeIngredientsPhoto(
                productName = productName,
                ingredientsText = ingredientsText,
                language = _selectedLanguage.value,
                madhhab = _selectedMadhhab.value
            )
            _activeProduct.value = product
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearScanHistory()
        }
    }
}

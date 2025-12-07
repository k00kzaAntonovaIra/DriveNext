package com.example.drivenext_antonova.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.drivenext_antonova.data.CarRepository
import com.example.drivenext_antonova.data.Result
import com.example.drivenext_antonova.data.CarData
import kotlinx.coroutines.launch

/**
 * ViewModel for home screen functionality
 */
class HomeViewModel : ViewModel() {

    private val _cars = MutableLiveData<List<CarData>>(emptyList())
    val cars: LiveData<List<CarData>> = _cars

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _searchResult = MutableLiveData<Result<List<CarData>>>()
    val searchResult: LiveData<Result<List<CarData>>> = _searchResult

    /**
     * Loads all available cars
     */
    fun loadCars() {
        _isLoading.value = true
        _errorMessage.value = null
        viewModelScope.launch {
            when (val result = CarRepository.fetchAllCars()) {
                is Result.Success -> {
                    _cars.value = result.data
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _cars.value = emptyList()
                    _isLoading.value = false
                    _errorMessage.value = result.message
                }
                is Result.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    /**
     * Searches cars by brand
     */
    fun searchBrand(brand: String) {
        if (brand.isBlank()) {
            _searchResult.value = Result.Success(emptyList())
            return
        }
        
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = CarRepository.searchByBrand(brand.trim())) {
                is Result.Success -> {
                    _isLoading.value = false
                    _searchResult.value = result
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _searchResult.value = result
                }
                is Result.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    /**
     * Searches cars by price range
     */
    fun searchByPriceRange(minPrice: Int, maxPrice: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = CarRepository.searchByPriceRange(minPrice, maxPrice)) {
                is Result.Success -> {
                    _isLoading.value = false
                    _searchResult.value = result
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _searchResult.value = result
                }
                is Result.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    /**
     * Clears search results
     */
    fun clearSearchResults() {
        _searchResult.value = Result.Success(emptyList())
    }

    /**
     * Clears error message
     */
    fun clearError() {
        _errorMessage.value = null
    }
}
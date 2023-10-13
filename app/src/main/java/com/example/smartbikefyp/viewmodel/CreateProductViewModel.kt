package com.example.smartbikefyp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.model.Product
import com.example.smartbikefyp.repository.Store
import com.example.smartbikefyp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val store: Store
) : ViewModel() {

    private val _createProductState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val createProductState: StateFlow<UiState<Boolean>>
        get() = _createProductState

    fun createProduct(product: Product, imageUri: Uri) {
        store.createProduct(product, imageUri)
            .onStart { _createProductState.value = UiState.Loading }
            .catch { _createProductState.value = UiState.Exception(it.message) }
            .onCompletion {
                if (it == null) {
                    _createProductState.value = UiState.Success(true)
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}
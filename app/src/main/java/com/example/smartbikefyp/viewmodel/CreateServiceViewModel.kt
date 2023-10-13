package com.example.smartbikefyp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.model.Service
import com.example.smartbikefyp.repository.Store
import com.example.smartbikefyp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class CreateServiceViewModel @Inject constructor(
    private val store: Store,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _createServiceState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val createServiceState: StateFlow<UiState<Boolean>>
        get() = _createServiceState

    fun createService(service: Service, imageUri: Uri) {
        store.createService(service, imageUri)
            .onStart {
                _createServiceState.value = UiState.Loading
            }
            .catch {
                _createServiceState.value = UiState.Exception(it.message)
            }
            .onCompletion {
                if (it == null)
                    _createServiceState.value = UiState.Success(true)
            }
            .launchIn(viewModelScope)
    }

}































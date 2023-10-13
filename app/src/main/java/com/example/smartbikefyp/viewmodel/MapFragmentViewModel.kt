package com.example.smartbikefyp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.model.MapRequestData
import com.example.smartbikefyp.model.map.GoogleMapRequestModel
import com.example.smartbikefyp.repository.Store
import com.example.smartbikefyp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class MapFragmentViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val store: Store
) : ViewModel() {

    private val _observeGoogleMapsNearBy = MutableStateFlow<UiState<GoogleMapRequestModel>>(UiState.Loading)
    val observeGoogleMapsNearBy: StateFlow<UiState<GoogleMapRequestModel>>
        get() = _observeGoogleMapsNearBy

    fun googleMapsNearBy(mapRequestData: MapRequestData) {
        store
            .googleMapsNearBy(mapRequestData)
            .onStart {
                _observeGoogleMapsNearBy.value = UiState.Loading
            }
            .catch {
                _observeGoogleMapsNearBy.value = UiState.Exception(it.message)
            }
            .onEach {
                _observeGoogleMapsNearBy.value = UiState.Success(it)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}
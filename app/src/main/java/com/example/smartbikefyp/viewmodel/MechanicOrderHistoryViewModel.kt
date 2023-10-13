package com.example.smartbikefyp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.ItemBoughtProduct
import com.example.smartbikefyp.model.ItemBoughtService
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
class MechanicOrderHistoryViewModel @Inject constructor(
    private val store: Store,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _boughtProductsState = MutableStateFlow<UiState<List<ItemBoughtProduct>>>(UiState.Loading)
    val boughtProductsState: StateFlow<UiState<List<ItemBoughtProduct>>>
        get() = _boughtProductsState

    private val _boughtServicesState = MutableStateFlow<UiState<List<ItemBoughtService>>>(UiState.Loading)
    val boughtServicesState: StateFlow<UiState<List<ItemBoughtService>>>
        get() = _boughtServicesState

    fun getBoughtProductsWithCreatorId(creatorId: String) {
        store.getBoughtProductsWithCreatorId(creatorId)
            .onStart {
                _boughtProductsState.value = UiState.Loading
            }
            .onEach {
                _boughtProductsState.value = UiState.Success(
                    it.filter { it.creatorUser!!.userId == creatorId }
                )
            }
            .catch {
                _boughtProductsState.value = UiState.Exception(it.message)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun getBoughtServicesWithCreatorId(creatorId: String) {
        store.getBoughtServicesWithCreatorId(creatorId)
            .onStart {
                _boughtServicesState.value = UiState.Loading
            }
            .onEach {
                _boughtServicesState.value = UiState.Success(
                    it.filter { it.creatorUser!!.userId == creatorId }
                )
            }
            .catch {
                _boughtServicesState.value = UiState.Exception(it.message)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}
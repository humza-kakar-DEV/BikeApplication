package com.example.smartbikefyp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.model.ProductUser
import com.example.smartbikefyp.model.ServiceUser
import com.example.smartbikefyp.repository.Store
import com.example.smartbikefyp.util.ProductCategory
import com.example.smartbikefyp.util.ServiceCategory
import com.example.smartbikefyp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class UserServiceViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val store: Store,
) : ViewModel() {

    private val _serviceUserState = MutableSharedFlow<UiState<ServiceUser>>()
    val serviceUserState: SharedFlow<UiState<ServiceUser>>
        get() = _serviceUserState

    val serviceCountState: StateFlow<Int?> =
        store.getServiceCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun getServices() {
        ServiceCategory.values().toList().asFlow()
            .onStart { _serviceUserState.emit(UiState.Loading) }
            .flatMapMerge {
                store.getServiceWithCategory(it.category)
            }.flatMapMerge {
                store.getServiceWithUser(it)
            }
            .onEach {
                delay(100)
                _serviceUserState.emit(UiState.Success(it))
            }
            .catch {
                _serviceUserState.emit(UiState.Exception(it.message))
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}
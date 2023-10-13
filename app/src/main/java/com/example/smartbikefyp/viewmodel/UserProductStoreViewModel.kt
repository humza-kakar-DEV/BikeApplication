package com.example.smartbikefyp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.ProductUser
import com.example.smartbikefyp.repository.Store
import com.example.smartbikefyp.util.ProductCategory
import com.example.smartbikefyp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

@HiltViewModel
class UserProductStoreViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val store: Store
) : ViewModel() {

    private val _productUserState = MutableSharedFlow<UiState<ProductUser>>()
    val productUserState: SharedFlow<UiState<ProductUser>>
        get() = _productUserState

    val productCountState: StateFlow<Int?> =
        store.getProductCount.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun getProducts() {
        ProductCategory.values().toList().asFlow()
            .onStart { _productUserState.emit(UiState.Loading) }
            .flatMapMerge {
                store.getProductWithCategory(it.category)
            }.flatMapMerge {
                store.getProductWithUser(it)
            }
            .onEach {
                delay(100)
                _productUserState.emit(UiState.Success(it))
            }
            .catch { _productUserState.emit(UiState.Exception(it.message)) }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}
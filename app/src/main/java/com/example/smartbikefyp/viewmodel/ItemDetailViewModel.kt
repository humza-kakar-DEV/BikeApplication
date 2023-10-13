package com.example.smartbikefyp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.ItemBoughtProduct
import com.example.smartbikefyp.model.ItemBoughtService
import com.example.smartbikefyp.repository.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val store: Store,
    @ApplicationContext private val context: Context,
): ViewModel() {

    fun buyProduct(itemBoughtProduct: ItemBoughtProduct) {
        store
            .buyProduct(itemBoughtProduct)
            .catch { context.log("data upload exception: ${it.message}") }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun buyService(itemBoughtService: ItemBoughtService) {
        store
            .buyService(itemBoughtService)
            .catch { context.log("data upload exception: ${it.message}") }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}
package com.example.smartbikefyp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.log
import com.example.smartbikefyp.repository.Authentication
import com.example.smartbikefyp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authentication: Authentication,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _signInState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val signInState: StateFlow<UiState<Boolean>>
        get() = _signInState

    fun signInUserEmailAndPassword(email: String, password: String) {
        authentication
            .signInUserEmailAndPassword(email, password)
            .onStart {
                _signInState.value = UiState.Loading
            }
            .onCompletion {
                if (it == null) {
//                    context.log("flow on complete all good!")
                    _signInState.value = UiState.Success(true)
                } else {
                    context.log("flow on complete exception: ${it.message}")
                }
            }
            .catch {
                _signInState.value = UiState.Exception(it.message)
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun signOutUser() {
        authentication.signOutUser()
    }

}
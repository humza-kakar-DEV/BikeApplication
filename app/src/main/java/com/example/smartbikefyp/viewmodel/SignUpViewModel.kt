package com.example.smartbikefyp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.repository.AuthenticationImp
import com.example.smartbikefyp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private var authenticationImp: AuthenticationImp,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _signUpState = MutableStateFlow<UiState<Boolean>>(UiState.Loading)
    val signUpState: StateFlow<UiState<Boolean>>
        get() = _signUpState

    fun signUpUserEmailAndPassword(user: User, imageUri: Uri) {
        authenticationImp.signUpUserEmailAndPassword(user, imageUri)
            .onStart {
                _signUpState.value = UiState.Loading
            }
            .catch {
                _signUpState.value = UiState.Exception(it.message)
            }
            .onCompletion {
                if (it == null) {
                    _signUpState.value = UiState.Success(true)
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

}
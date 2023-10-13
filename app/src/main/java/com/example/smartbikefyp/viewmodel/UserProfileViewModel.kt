package com.example.smartbikefyp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.repository.Authentication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val authentication: Authentication,
    @ApplicationContext private val context: Context,
): ViewModel() {

    fun updateUserData(user: User, imageUri: Uri?) {
        authentication.updateUserData(user, imageUri)
            .catch { context.log("update data exception: ${it.message}") }
            .onCompletion {
                if (it == null) {
//                    flow completed without exception
                }
            }
            .launchIn(viewModelScope)
    }

}


























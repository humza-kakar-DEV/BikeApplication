package com.example.smartbikefyp.repository

import android.net.Uri
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.util.UiState
import com.example.smartbikefyp.util.UserRoleType
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.descriptors.PrimitiveKind


interface Authentication {

    val currentUser: Flow<UserRoleType<User>>

    fun signUpUserEmailAndPassword(user: User, imageUri: Uri): Flow<Nothing>

    fun signInUserEmailAndPassword(email: String, password: String): Flow<Nothing>

    fun signOutUser()

    fun updateUserData(user: User, imageUri: Uri?): Flow<Nothing>

    fun updateMechanicData(mechanic: User, imageUri: Uri?): Flow<Nothing>

}



























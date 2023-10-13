package com.example.smartbikefyp.repository

import android.content.Context
import android.net.Uri
import com.example.smartbikefyp.getMimeType
import com.example.smartbikefyp.log
import com.example.smartbikefyp.model.User
import com.example.smartbikefyp.util.UserRoleType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationImp @Inject constructor(
    private var firebaseAuth: FirebaseAuth,
    private var firebaseStorage: FirebaseStorage,
    private var firebaseFirestore: FirebaseFirestore,
    @ApplicationContext val context: Context,
) : Authentication {

    override val currentUser: Flow<UserRoleType<User>> = callbackFlow<UserRoleType<User>> {
        firebaseAuth.currentUser ?: close(UserRoleType.Exception.USER_NOT_FOUND_EXCEPTION)
        val snapshotListener =
            firebaseFirestore.collection("user_info")
                .whereEqualTo("email", firebaseAuth.currentUser?.email)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        close(e)
                        return@addSnapshotListener
                    }
                    if ((snapshot != null) and (!snapshot?.isEmpty!!)) {
                        snapshot.toObjects(User::class.java).map {
                            if (it.role == "user") {
                                trySend(UserRoleType.User(it))
                            } else {
                                trySend(UserRoleType.Mechanic(it))
                            }
                        }
                    }
                }
        awaitClose {
            snapshotListener.remove()
        }
    }
        .catch {
            emit(UserRoleType.Exception(it.message!!))
        }

    override fun signUpUserEmailAndPassword(user: User, imageUri: Uri) =
        flow<Nothing> {
            user.apply {
                val imageDownloadUri = firebaseStorage
                    .reference
                    .child("images/${System.currentTimeMillis()}.${context.getMimeType(imageUri)}")
                    .putFile(imageUri)
                    .await()
                    .storage
                    .downloadUrl
                    .await()

                userId = firebaseFirestore.collection("user_info").document().id
                this.imageUri = imageDownloadUri.toString()

                firebaseAuth
                    .createUserWithEmailAndPassword(user.email, user.password)
                    .await()

                firebaseFirestore
                    .collection("user_info")
                    .document(user.userId)
                    .set(user)
                    .await()
            }
        }

    override fun signInUserEmailAndPassword(email: String, password: String) = flow<Nothing> {
        firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await()
    }

    override fun signOutUser() {
        firebaseAuth.signOut()
    }

    override fun updateUserData(user: User, imageUri: Uri?) = flow<Nothing> {
        imageUri?.let {
            val imageDownloadUri = firebaseStorage
                .reference
                .child("images/${System.currentTimeMillis()}.${context.getMimeType(imageUri)}")
                .putFile(imageUri)
                .await()
                .storage
                .downloadUrl
                .await()

            user.imageUri = imageDownloadUri.toString()
        }

        firebaseFirestore.collection("user_info")
            .document(user.userId)
            .set(user)
            .await()
    }

    override fun updateMechanicData(mechanic: User, imageUri: Uri?) = flow<Nothing> {
        imageUri?.let {
            val imageDownloadUri = firebaseStorage
                .reference
                .child("images/${System.currentTimeMillis()}.${context.getMimeType(imageUri)}")
                .putFile(imageUri)
                .await()
                .storage
                .downloadUrl
                .await()

            mechanic.imageUri = imageDownloadUri.toString()
        }

        firebaseFirestore.collection("user_info")
            .document(mechanic.userId)
            .set(mechanic)
            .await()
    }


}
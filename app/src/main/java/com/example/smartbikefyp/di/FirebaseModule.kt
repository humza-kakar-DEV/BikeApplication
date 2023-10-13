package com.example.smartbikefyp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    @Singleton
    fun getFirebaseAuth (): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun getFirebaseFirestore (): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun getFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

}
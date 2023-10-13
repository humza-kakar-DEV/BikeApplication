package com.example.smartbikefyp.di

import com.example.smartbikefyp.repository.Authentication
import com.example.smartbikefyp.repository.AuthenticationImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthenticationRepositoryModule {

    @Binds
    @Singleton
    abstract fun getAuthenticationRepository (
        authenticationImp: AuthenticationImp
    ): Authentication

}
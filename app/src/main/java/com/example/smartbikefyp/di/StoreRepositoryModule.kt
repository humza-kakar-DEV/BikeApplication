package com.example.smartbikefyp.di

import com.example.smartbikefyp.repository.Authentication
import com.example.smartbikefyp.repository.AuthenticationImp
import com.example.smartbikefyp.repository.Store
import com.example.smartbikefyp.repository.StoreImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StoreRepositoryModule {

    @Binds
    @Singleton
    abstract fun getStoreRepository (
        storeImp: StoreImp
    ): Store

}
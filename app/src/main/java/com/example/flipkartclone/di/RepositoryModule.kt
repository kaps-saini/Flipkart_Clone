package com.example.flipkartclone.di

import com.example.flipkartclone.data.repositoryImpl.ItemsRepositoryImpl
import com.example.flipkartclone.domain.interfaces.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepository(
        itemsRepositoryImpl: ItemsRepositoryImpl
    ): Repository
}
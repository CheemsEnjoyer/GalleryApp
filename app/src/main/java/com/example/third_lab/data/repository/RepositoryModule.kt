package com.example.third_lab.data.repository

import com.example.third_lab.domain.port.CategoryRepository
import com.example.third_lab.domain.port.PhotoRepository
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
    abstract fun bindCategoryRepository(
        repositoryImpl: CategoryRepositoryImp
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun buildPhotoRepository(
        repositoryImpl: PhotoRepositoryImp
    ): PhotoRepository
}

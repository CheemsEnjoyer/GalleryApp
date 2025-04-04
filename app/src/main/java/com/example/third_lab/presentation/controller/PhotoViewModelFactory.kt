package com.example.third_lab.presentation.controller

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.third_lab.PhotoViewModel
import com.example.third_lab.domain.usecase.photo.LoadPhotosUseCase
import com.example.third_lab.domain.usecase.SavePhotoUseCase
import com.example.third_lab.domain.usecase.category.LoadCategoriesUseCase
import com.example.third_lab.domain.usecase.photo.DeletePhotoUseCase
import com.example.third_lab.domain.usecase.photo.FilterPhotosByCategoryUseCase

class PhotoViewModelFactory(
    private val application: Application,
    private val loadPhotosUseCase: LoadPhotosUseCase,
    private val savePhotoUseCase: SavePhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val filterPhotosUseCase: FilterPhotosByCategoryUseCase,
    private val loadCategoriesUseCase: LoadCategoriesUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PhotoViewModel(
            application,
            loadPhotosUseCase,
            savePhotoUseCase,
            deletePhotoUseCase,
            filterPhotosUseCase,
            loadCategoriesUseCase
        ) as T
    }

}

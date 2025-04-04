package com.example.third_lab.presentation.controller

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.third_lab.CategoryViewModel
import com.example.third_lab.domain.usecase.DeleteCategoryUseCase
import com.example.third_lab.domain.usecase.UpdateCategoryUseCase
import com.example.third_lab.domain.usecase.category.LoadCategoriesUseCase

class CategoryViewModelFactory(
    private val app: Application,
    private val load: LoadCategoriesUseCase,
    private val update: UpdateCategoryUseCase,
    private val delete: DeleteCategoryUseCase
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        return CategoryViewModel(app, load, update, delete) as T
    }
}

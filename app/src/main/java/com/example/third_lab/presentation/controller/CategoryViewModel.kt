package com.example.third_lab

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.third_lab.domain.entity.Category
import com.example.third_lab.domain.usecase.DeleteCategoryUseCase
import com.example.third_lab.domain.usecase.UpdateCategoryUseCase
import com.example.third_lab.domain.usecase.category.LoadCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    application: Application,
    private val loadCategoriesUseCase: LoadCategoriesUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : AndroidViewModel(application) {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    fun loadCategories() {
        viewModelScope.launch {
            loadCategoriesUseCase().onSuccess { result ->
                _categories.value = result
            }
        }
    }

    fun updateCategory(categoryId: Long, newName: String) {
        viewModelScope.launch {
            updateCategoryUseCase(categoryId, newName)
            loadCategories()
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            deleteCategoryUseCase(category.id)
            loadCategories()
        }
    }
}

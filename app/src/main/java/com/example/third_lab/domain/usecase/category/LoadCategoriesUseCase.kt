package com.example.third_lab.domain.usecase.category

import com.example.third_lab.domain.entity.Category
import com.example.third_lab.domain.port.CategoryRepository

class LoadCategoriesUseCase(private val repository: CategoryRepository) {
    operator fun invoke(): Result<List<Category>> = runCatching {
        repository.getCategories()
    }
}

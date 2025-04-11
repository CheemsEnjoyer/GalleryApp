package com.example.third_lab.domain.usecase.category

import com.example.third_lab.domain.entity.Category
import com.example.third_lab.domain.port.CategoryRepository
import javax.inject.Inject

class LoadCategoriesUseCase @Inject constructor(private val repository: CategoryRepository) {
    operator fun invoke(): Result<List<Category>> = runCatching {
        repository.getCategories()
    }
}

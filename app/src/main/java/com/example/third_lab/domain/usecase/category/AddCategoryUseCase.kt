package com.example.third_lab.domain.usecase.category

import com.example.third_lab.domain.port.CategoryRepository

class AddCategoryUseCase(private val repository: CategoryRepository) {
    operator fun invoke(category: String, privacyLevel: String): Result<Unit> {
        if (category.isEmpty()) return Result.failure(Exception("Категория не может быть пустой"))
        val id = repository.addCategory(category)
        return if (id != -1L) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Ошибка при добавлении категории"))
        }
    }
}

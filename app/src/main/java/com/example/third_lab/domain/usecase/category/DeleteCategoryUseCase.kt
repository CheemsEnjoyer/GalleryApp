package com.example.third_lab.domain.usecase

import com.example.third_lab.domain.port.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCase  @Inject constructor(private val repository: CategoryRepository) {
    operator fun invoke(categoryId: Long): Result<Unit> {
        val rowsDeleted = repository.deleteCategory(categoryId)
        return if (rowsDeleted > 0) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Ошибка при удалении категории"))
        }
    }
}

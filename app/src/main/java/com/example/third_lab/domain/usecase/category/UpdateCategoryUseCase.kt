package com.example.third_lab.domain.usecase

import com.example.third_lab.domain.port.CategoryRepository
import javax.inject.Inject

class UpdateCategoryUseCase @Inject constructor(private val repository: CategoryRepository) {
    operator fun invoke(categoryId: Long, newName: String): Result<Unit> {
        if (newName.isBlank()) {
            return Result.failure(Exception("Название не может быть пустым"))
        }
        if (newName.contains(Regex("[?.!,;:]"))) {
            return Result.failure(Exception("Название содержит недопустимые символы"))
        }

        val rowsUpdated = repository.updateCategoryName(categoryId, newName)
        return if (rowsUpdated > 0) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Ошибка при обновлении категории"))
        }
    }
}

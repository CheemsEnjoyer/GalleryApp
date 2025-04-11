package com.example.third_lab.domain.usecase

import android.graphics.Bitmap
import com.example.third_lab.domain.port.PhotoRepository
import javax.inject.Inject

class SavePhotoUseCase  @Inject constructor(private val repository: PhotoRepository) {
    operator fun invoke(bitmap: Bitmap, categoryName: String): Result<Unit> {
        if (categoryName.isEmpty()) {
            return Result.failure(Exception("Категория не может быть пустой"))
        }
        val success = repository.savePhoto(bitmap, categoryName)
        return if (success) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Ошибка при сохранении фото"))
        }
    }
}

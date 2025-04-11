package com.example.third_lab.domain.usecase.photo

import com.example.third_lab.domain.entity.Photo
import com.example.third_lab.domain.port.PhotoRepository
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(private val repository: PhotoRepository) {
    operator fun invoke(photo: Photo): Result<Unit> {
        val rows = repository.deletePhoto(photo.id)
        return if (rows > 0) Result.success(Unit) else Result.failure(Exception("Ошибка удаления"))
    }
}

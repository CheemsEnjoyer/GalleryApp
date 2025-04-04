package com.example.third_lab.domain.usecase.photo

import com.example.third_lab.domain.entity.Photo
import com.example.third_lab.domain.port.PhotoRepository

class FilterPhotosByCategoryUseCase(
    private val repository: PhotoRepository
) {
    operator fun invoke(categoryId: Long?): List<Photo> {
        return if (categoryId == null) {
            repository.getAllPhotos()
        } else {
            repository.getPhotosByCategoryId(categoryId)
        }
    }
}

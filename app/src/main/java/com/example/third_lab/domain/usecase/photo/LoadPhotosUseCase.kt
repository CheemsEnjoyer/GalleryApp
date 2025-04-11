package com.example.third_lab.domain.usecase.photo

import com.example.third_lab.domain.entity.Photo
import com.example.third_lab.domain.port.PhotoRepository
import javax.inject.Inject

class LoadPhotosUseCase @Inject constructor(private val repository: PhotoRepository) {
    operator fun invoke(): Result<List<Photo>> = runCatching {
        repository.getAllPhotos()
    }
}

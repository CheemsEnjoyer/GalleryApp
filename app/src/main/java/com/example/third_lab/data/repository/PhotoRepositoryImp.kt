package com.example.third_lab.data.repository

import android.graphics.Bitmap
import com.example.third_lab.data.datasource.PhotoLocalDataSource
import com.example.third_lab.domain.entity.Photo
import com.example.third_lab.domain.port.PhotoRepository

class PhotoRepositoryImp(
    private val localDataSource: PhotoLocalDataSource
) : PhotoRepository {

    override fun getAllPhotos(): List<Photo> {
        return localDataSource.getAllPhotos()
    }

    override fun getPhotosByCategoryId(categoryId: Long): List<Photo> {
        return localDataSource.getPhotosByCategoryId(categoryId)
    }

    override fun deletePhoto(photoId: Long): Int {
        return localDataSource.deletePhoto(photoId)
    }

    override fun savePhoto(bitmap: Bitmap, category: String): Boolean {
        return localDataSource.savePhoto(bitmap, category)
    }
}

package com.example.third_lab.domain.port

import android.graphics.Bitmap
import com.example.third_lab.domain.entity.Photo

interface PhotoRepository  {
    fun getAllPhotos(): List<Photo>
    fun getPhotosByCategoryId(categoryId: Long): List<Photo>
    fun deletePhoto(photoId: Long): Int
    fun savePhoto(bitmap: Bitmap, category: String): Boolean
}

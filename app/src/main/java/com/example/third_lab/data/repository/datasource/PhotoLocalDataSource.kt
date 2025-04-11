package com.example.third_lab.data.datasource

import android.content.Context
import android.graphics.Bitmap
import com.example.third_lab.db.DatabaseWorker
import com.example.third_lab.domain.entity.Photo
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class PhotoLocalDataSource @Inject constructor(
    @ApplicationContext context: Context
) {
    private val db = DatabaseWorker(context)
    private val filesDir: File = context.filesDir

    fun getAllPhotos(): List<Photo> {
        return db.getAllPhotos()
    }

    fun getPhotosByCategoryId(categoryId: Long): List<Photo> {
        return db.getPhotosByCategoryId(categoryId)
    }

    fun deletePhoto(photoId: Long): Int {
        return db.deletePhoto(photoId)
    }

    fun savePhoto(bitmap: Bitmap, categoryName: String): Boolean {
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)
        return try {
            FileOutputStream(file).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            }
            val categoryId = db.getCategoryIdByName(categoryName)
            if (categoryId != null) {
                val photoId = db.addPhotoForCategory(categoryId, file.path)
                photoId != -1L
            } else {
                false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }
}

package com.example.third_lab

import android.app.Application
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.third_lab.domain.entity.Category
import com.example.third_lab.domain.entity.Photo
import com.example.third_lab.domain.usecase.photo.LoadPhotosUseCase
import com.example.third_lab.domain.usecase.SavePhotoUseCase
import com.example.third_lab.domain.usecase.category.LoadCategoriesUseCase
import com.example.third_lab.domain.usecase.photo.DeletePhotoUseCase
import com.example.third_lab.domain.usecase.photo.FilterPhotosByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    application: Application,
    private val loadPhotosUseCase: LoadPhotosUseCase,
    private val savePhotoUseCase: SavePhotoUseCase,
    private val deletePhotoUseCase: DeletePhotoUseCase,
    private val filterPhotosUseCase: FilterPhotosByCategoryUseCase,
    private val loadCategoriesUseCase: LoadCategoriesUseCase
) : AndroidViewModel(application) {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos.asStateFlow()

    private val _photoBitmap = MutableStateFlow<Bitmap?>(null)
    val photoBitmap: StateFlow<Bitmap?> = _photoBitmap.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    fun loadPhotos() {
        viewModelScope.launch {
            loadPhotosUseCase().onSuccess { _photos.value = it }
        }
    }

    fun deletePhoto(photo: Photo) {
        viewModelScope.launch {
            deletePhotoUseCase(photo).onSuccess {
                loadPhotos()
                Toast.makeText(getApplication(), "Фото удалено", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(getApplication(), it.message ?: "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun savePhoto(bitmap: Bitmap, category: String?) {
        if (category.isNullOrEmpty()) {
            Toast.makeText(getApplication(), "Пожалуйста, выберите категорию", Toast.LENGTH_SHORT).show()
            return
        }

        viewModelScope.launch {
            savePhotoUseCase(bitmap, category).onSuccess {
                loadPhotos()
                _photoBitmap.value = bitmap
                Toast.makeText(getApplication(), "Фото сохранено", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(getApplication(), it.message ?: "Ошибка", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun filterPhotosByCategory(categoryId: Long?) {
        viewModelScope.launch {
            val filtered = filterPhotosUseCase(categoryId)
            _photos.value = filtered
        }
    }

    fun loadCategories() {
        viewModelScope.launch {
            loadCategoriesUseCase().onSuccess { result ->
                _categories.value = result
            }
        }
    }
}

package com.example.third_lab

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.third_lab.data.datasource.CategoryLocalDataSource
import com.example.third_lab.data.datasource.PhotoLocalDataSource
import com.example.third_lab.data.repository.CategoryRepositoryImp
import com.example.third_lab.data.repository.PhotoRepositoryImp
import com.example.third_lab.domain.entity.Category
import com.example.third_lab.domain.usecase.photo.LoadPhotosUseCase
import com.example.third_lab.domain.usecase.SavePhotoUseCase
import com.example.third_lab.domain.usecase.category.LoadCategoriesUseCase
import com.example.third_lab.domain.usecase.photo.DeletePhotoUseCase
import com.example.third_lab.domain.usecase.photo.FilterPhotosByCategoryUseCase
import com.example.third_lab.presentation.controller.PhotoViewModelFactory
import kotlinx.coroutines.launch

class PhotoFragment : Fragment() {

    private lateinit var photoImageView: ImageView
    private lateinit var categorySpinner: Spinner
    private lateinit var takePhotoButton: Button
    private lateinit var photoViewModel: PhotoViewModel
    private var categoryList: List<Category> = emptyList()

    private val takePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data")
            if (imageBitmap is Bitmap) {
                val selectedPosition = categorySpinner.selectedItemPosition
                val selectedCategoryName = categoryList.getOrNull(selectedPosition)?.name
                photoViewModel.savePhoto(imageBitmap, selectedCategoryName)
            } else {
                Toast.makeText(requireContext(), "Не удалось получить фото", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        photoImageView = view.findViewById(R.id.photoImageView)
        categorySpinner = view.findViewById(R.id.categorySpinner)
        takePhotoButton = view.findViewById(R.id.takePhotoButton)
        val application = requireActivity().application
        val photoRepo = PhotoRepositoryImp(PhotoLocalDataSource(requireContext()))
        val categoryRepo = CategoryRepositoryImp(CategoryLocalDataSource(requireContext()))

        val photoFactory = PhotoViewModelFactory(
            application,
            LoadPhotosUseCase(photoRepo),
            SavePhotoUseCase(photoRepo),
            DeletePhotoUseCase(photoRepo),
            FilterPhotosByCategoryUseCase(photoRepo),
            LoadCategoriesUseCase(categoryRepo)
        )


        photoViewModel = ViewModelProvider(this, photoFactory)[PhotoViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoViewModel.categories.collect { categories ->
                    categoryList = categories  // сохраняем категории отдельно
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        categories.map { it.name }
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    categorySpinner.adapter = adapter

                    if (categories.isEmpty()) {
                        Toast.makeText(requireContext(), "Нет доступных категорий", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoViewModel.photoBitmap.collect { bitmap ->
                    bitmap?.let { photoImageView.setImageBitmap(it) }
                }
            }
        }


        takePhotoButton.setOnClickListener {
            val selectedPosition = categorySpinner.selectedItemPosition
            val selectedCategoryName = categoryList.getOrNull(selectedPosition)?.name

            val takePictureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                takePhotoLauncher.launch(takePictureIntent)
            }
        }


        photoViewModel.loadPhotos()
        photoViewModel.loadCategories()
    }

}

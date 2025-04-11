package com.example.third_lab

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.third_lab.databinding.FragmentCameraBinding
import com.example.third_lab.domain.entity.Category
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoFragment : Fragment() {

    private lateinit var photoImageView: ImageView
    private lateinit var categorySpinner: Spinner
    private lateinit var takePhotoButton: Button
    private val photoViewModel: PhotoViewModel by viewModels()
    private lateinit var binding: FragmentCameraBinding
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
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        photoImageView = binding.photoImageView
        categorySpinner = binding.categorySpinner
        takePhotoButton = binding.takePhotoButton

        // До этого тут был код создания PhotoRepository, CategoryRepository, UseCase'ов и фабрики

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoViewModel.categories.collect { categories ->
                    categoryList = categories
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
            val takePictureIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
                takePhotoLauncher.launch(takePictureIntent)
            }
        }

        photoViewModel.loadPhotos()
        photoViewModel.loadCategories()
    }
}

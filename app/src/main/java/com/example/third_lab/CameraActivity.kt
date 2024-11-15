package com.example.third_lab

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraActivity : AppCompatActivity() {

    private lateinit var photoImageView: ImageView
    private lateinit var categorySpinner: Spinner
    private lateinit var databaseWorker: DatabaseWorker
    private val requestCodeTakePhoto = 1
    private var photoFilePath: String? = null // Путь для последнего сохраненного фото

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        photoImageView = findViewById(R.id.photoImageView)
        categorySpinner = findViewById(R.id.categorySpinner)
        databaseWorker = DatabaseWorker(this)

        loadCategories()

        val takePhotoButton = findViewById<Button>(R.id.takePhotoButton)
        takePhotoButton.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, requestCodeTakePhoto)
            }
        }

        // Загрузка последнего сохраненного фото (если путь сохранен)
        loadLastSavedPhoto()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
        bottomNavigationView.selectedItemId = R.id.menu_camera
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                R.id.menu_privacy -> {
                    val intent = Intent(this, PrivacyActivity::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                R.id.menu_category -> {
                    val intent = Intent(this, CategoryActivity::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadCategories() {
        val categoryNames = databaseWorker.getAllCategories()
        if (categoryNames.isEmpty()) {
            Toast.makeText(this, "Нет доступных категорий", Toast.LENGTH_SHORT).show()
        }
        // Заполнение Spinner именами категорий
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeTakePhoto && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            photoImageView.setImageBitmap(imageBitmap)

            // Сохранение фотографии и обновление пути к файлу
            savePhoto(imageBitmap)
        }
    }

    private fun savePhoto(bitmap: Bitmap) {
        val selectedCategory = categorySpinner.selectedItem?.toString()
        if (selectedCategory.isNullOrEmpty()) {
            Toast.makeText(this, "Пожалуйста, выберите категорию", Toast.LENGTH_SHORT).show()
            return
        }

        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        val file = File(filesDir, fileName)
        try {
            FileOutputStream(file).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            }

            val categoryId = databaseWorker.getCategoryIdByName(selectedCategory)
            if (categoryId != null) {
                val photoId = databaseWorker.addPhotoForCategory(categoryId, file.path)
                if (photoId != -1L) {
                    Toast.makeText(this, "Фото сохранено с категорией $selectedCategory", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Ошибка при сохранении фото", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Ошибка при сохранении фото", Toast.LENGTH_SHORT).show()
            }

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при сохранении фото", Toast.LENGTH_SHORT).show()
        }
    }


    private fun loadLastSavedPhoto() {
        photoFilePath?.let { path ->
            val photoFile = File(path)
            if (photoFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(photoFile.path)
                photoImageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Фото не найдено", Toast.LENGTH_SHORT).show()
            }
        }
    }

}

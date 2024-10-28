package com.example.third_lab

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CameraActivity : AppCompatActivity() {

    private lateinit var photoImageView: ImageView
    private val requestCodeTakePhoto = 1
    private val photoFileName = "photo.jpg"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        photoImageView = findViewById(R.id.photoImageView)
        val takePhotoButton = findViewById<Button>(R.id.takePhotoButton)

        takePhotoButton.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, requestCodeTakePhoto)
            }
        }

        loadPhoto()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_menu)
        bottomNavigationView.selectedItemId = R.id.menu_history
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
                R.id.menu_history -> {
                    val intent = Intent(this, HistoryActivity::class.java)
                    finish()
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCodeTakePhoto && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            photoImageView.setImageBitmap(imageBitmap)

            savePhoto(imageBitmap)
        }
    }

    private fun savePhoto(bitmap: Bitmap) {
        val file = File(filesDir, photoFileName)
        try {
            FileOutputStream(file).use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
            }
            Toast.makeText(this, "Фото сохранено", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Ошибка при сохранении фото", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPhoto() {
        val photoFile = File(filesDir, photoFileName)
        if (photoFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(photoFile.path)
            photoImageView.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "Фото не найдено", Toast.LENGTH_SHORT).show()
        }
    }
}

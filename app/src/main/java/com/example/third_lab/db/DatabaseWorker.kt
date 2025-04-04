package com.example.third_lab.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getLongOrNull
import com.example.third_lab.domain.entity.Category
import com.example.third_lab.domain.entity.Photo

class DatabaseWorker(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_CATEGORIES)
        db.execSQL(CREATE_TABLE_PHOTOS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_PHOTOS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
            onCreate(db)
        }
    }

    // Добавление новой категории
    fun addCategory(categoryName: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_NAME, categoryName)
        }
        return db.insert(TABLE_CATEGORIES, null, values)
    }

    // Добавление новой фотографии с опциональной категорией
    fun addPhotoForCategory(categoryId: Long, photoPath: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("category_id", categoryId)
            put("photo_path", photoPath)
        }
        return db.insert("photos", null, values)
    }

    // Получение всех категорий
    fun getAllCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_CATEGORY_ID, $COLUMN_CATEGORY_NAME FROM $TABLE_CATEGORIES", null)

        cursor.use {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))
                val name = it.getString(it.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME))
                categories.add(Category(id, name))
            }
        }
        return categories
    }



    // Получение всех фотографий
    fun getAllPhotos(): List<Photo> {
        val photos = mutableListOf<Photo>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PHOTOS,
            null, // Все столбцы
            null, null, null, null, null
        )

        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_ID))
                val categoryId = cursor.getLongOrNull(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_CATEGORY_ID))
                val photoPath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_PATH))

                photos.add(Photo(id, categoryId, photoPath))
            }
        }

        return photos
    }

    // Получение фотографий по идентификатору категории
    fun getPhotosByCategoryId(categoryId: Long): List<Photo> {
        val photos = mutableListOf<Photo>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_PHOTOS,
            null,
            "$COLUMN_PHOTO_CATEGORY_ID = ?",
            arrayOf(categoryId.toString()),
            null, null, null
        )

        cursor.use {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_ID))
                val photoPath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHOTO_PATH))
                photos.add(Photo(id, categoryId, photoPath))
            }
        }

        return photos
    }

    // Удаление категории по ID
    fun deleteCategory(categoryId: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_CATEGORIES, "$COLUMN_CATEGORY_ID = ?", arrayOf(categoryId.toString()))
    }

    // Удаление фотографии по ID
    fun deletePhoto(photoId: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_PHOTOS, "$COLUMN_PHOTO_ID = ?", arrayOf(photoId.toString()))
    }

    // Получение ID категории по ее имени
    fun getCategoryIdByName(categoryName: String): Long? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CATEGORIES,
            arrayOf(COLUMN_CATEGORY_ID),
            "$COLUMN_CATEGORY_NAME = ?",
            arrayOf(categoryName),
            null, null, null
        )
        cursor.use {
            if (cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_ID))
            }
        }
        return null
    }

    // Обновление названия категории по ID
    fun updateCategoryName(categoryId: Long, newName: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_NAME, newName)
        }
        return db.update(TABLE_CATEGORIES, values, "$COLUMN_CATEGORY_ID = ?", arrayOf(categoryId.toString()))
    }


    companion object {
        private const val DATABASE_VERSION = 7
        private const val DATABASE_NAME = "categories.db"

        // Таблица категорий
        const val TABLE_CATEGORIES = "categories"
        const val COLUMN_CATEGORY_ID = "id"
        const val COLUMN_CATEGORY_NAME = "name"

        // Таблица фотографий
        const val TABLE_PHOTOS = "photos"
        const val COLUMN_PHOTO_ID = "id"
        const val COLUMN_PHOTO_PATH = "photo_path"
        const val COLUMN_PHOTO_CATEGORY_ID = "category_id"

        private const val CREATE_TABLE_CATEGORIES = """
            CREATE TABLE $TABLE_CATEGORIES (
                $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL
            )
        """

        private const val CREATE_TABLE_PHOTOS = """
            CREATE TABLE $TABLE_PHOTOS (
                $COLUMN_PHOTO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PHOTO_PATH TEXT NOT NULL,
                $COLUMN_PHOTO_CATEGORY_ID INTEGER,
                FOREIGN KEY($COLUMN_PHOTO_CATEGORY_ID) REFERENCES $TABLE_CATEGORIES($COLUMN_CATEGORY_ID) ON DELETE SET NULL
            )
        """
    }
}

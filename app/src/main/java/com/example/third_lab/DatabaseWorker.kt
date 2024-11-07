package com.example.third_lab

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseWorker(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_CATEGORIES)
        db.execSQL(CREATE_TABLE_DESCRIPTIONS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DESCRIPTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        onCreate(db)
    }

    // Добавление новой категории в таблицу categories
    fun addCategory(categoryName: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_NAME, categoryName)
        }
        return db.insert(TABLE_CATEGORIES, null, values)
    }

    // Добавление описания, связанного с категорией
    fun addDescriptionForCategory(categoryId: Long, description: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DESCRIPTION_TEXT, description)
            put(COLUMN_DESCRIPTION_CATEGORY_ID, categoryId)
        }
        return db.insert(TABLE_DESCRIPTIONS, null, values)
    }


    fun getAllCategoriesWithDescriptions(): MutableList<Pair<String, String>> {
        val categoriesWithDescriptions = mutableListOf<Pair<String, String>>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            """
            SELECT ${TABLE_CATEGORIES}.${COLUMN_CATEGORY_NAME}, ${TABLE_DESCRIPTIONS}.${COLUMN_DESCRIPTION_TEXT}
            FROM $TABLE_CATEGORIES
            LEFT JOIN $TABLE_DESCRIPTIONS 
            ON ${TABLE_CATEGORIES}.${COLUMN_CATEGORY_ID} = ${TABLE_DESCRIPTIONS}.${COLUMN_DESCRIPTION_CATEGORY_ID}
        """, null
        )

        cursor.use {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION_TEXT)) ?: ""
                categoriesWithDescriptions.add(name to description)
            }
        }

        return categoriesWithDescriptions
    }

    // Удаляет категорию и все связанные с ней описания
    fun deleteCategoryWithDescription(categoryName: String): Int {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val categoryId = getCategoryIdByName(categoryName)
            if (categoryId != null) {
                // Удаляем описания, связанные с категорией
                deleteDescriptionByCategoryId(categoryId)
                // Удаляем саму категорию
                val deletedRows = db.delete(TABLE_CATEGORIES, "$COLUMN_CATEGORY_ID = ?", arrayOf(categoryId.toString()))
                db.setTransactionSuccessful()
                return deletedRows
            }
            return 0
        } finally {
            db.endTransaction()
        }
    }
    // Получение ID категории по её имени
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

    // Обновление имени категории по ID
    fun updateCategoryName(categoryId: Long, newName: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CATEGORY_NAME, newName)
        }
        return db.update(TABLE_CATEGORIES, values, "$COLUMN_CATEGORY_ID = ?", arrayOf(categoryId.toString()))
    }

    // Обновление описания по ID категории
    fun updateDescription(categoryId: Long, newDescription: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DESCRIPTION_TEXT, newDescription)
        }
        return db.update(TABLE_DESCRIPTIONS, values, "$COLUMN_DESCRIPTION_CATEGORY_ID = ?", arrayOf(categoryId.toString()))
    }

    // Удаление категории по ID
    fun deleteCategory(categoryId: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_CATEGORIES, "$COLUMN_CATEGORY_ID = ?", arrayOf(categoryId.toString()))
    }

    // Удаление описаний по ID категории
    fun deleteDescriptionByCategoryId(categoryId: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_DESCRIPTIONS, "$COLUMN_DESCRIPTION_CATEGORY_ID = ?", arrayOf(categoryId.toString()))
    }

    // Обновляет название категории и описание, связанное с ней
    fun updateCategoryWithDescription(oldName: String, newName: String, newDescription: String): Int {
        val db = writableDatabase
        db.beginTransaction()
        try {
            val categoryId = getCategoryIdByName(oldName)
            if (categoryId != null) {
                // Обновляем имя категории
                updateCategoryName(categoryId, newName)
                // Обновляем описание
                updateDescription(categoryId, newDescription)
                db.setTransactionSuccessful()
                return 1 // Возвращаем 1, если обновление прошло успешно
            }
            return 0
        } finally {
            db.endTransaction()
        }
    }


    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "categories.db"

        // Таблица categories
        const val TABLE_CATEGORIES = "categories"
        const val COLUMN_CATEGORY_ID = "id"
        const val COLUMN_CATEGORY_NAME = "name"

        // Таблица descriptions
        const val TABLE_DESCRIPTIONS = "descriptions"
        const val COLUMN_DESCRIPTION_ID = "id"
        const val COLUMN_DESCRIPTION_TEXT = "description"
        const val COLUMN_DESCRIPTION_CATEGORY_ID = "category_id" // Внешний ключ на categories.id

        // SQL для создания таблицы categories
        private const val CREATE_TABLE_CATEGORIES = """
            CREATE TABLE $TABLE_CATEGORIES (
                $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL
            )
        """

        // SQL для создания таблицы descriptions
        private const val CREATE_TABLE_DESCRIPTIONS = """
            CREATE TABLE $TABLE_DESCRIPTIONS (
                $COLUMN_DESCRIPTION_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DESCRIPTION_TEXT TEXT NOT NULL,
                $COLUMN_DESCRIPTION_CATEGORY_ID INTEGER,
                FOREIGN KEY($COLUMN_DESCRIPTION_CATEGORY_ID) REFERENCES $TABLE_CATEGORIES($COLUMN_CATEGORY_ID)
            )
        """
    }
}

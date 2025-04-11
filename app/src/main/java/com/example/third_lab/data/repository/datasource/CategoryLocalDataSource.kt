package com.example.third_lab.data.datasource

import android.content.Context
import com.example.third_lab.db.DatabaseWorker
import com.example.third_lab.domain.entity.Category
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    @ApplicationContext context: Context) {

    private val db = DatabaseWorker(context)

    fun getAllCategories(): List<Category> {
        return db.getAllCategories()
    }

    fun addCategory(name: String): Long {
        return db.addCategory(name)
    }

    fun deleteCategory(id: Long): Int {
        return db.deleteCategory(id)
    }

    fun updateCategoryName(id: Long, newName: String): Int {
        return db.updateCategoryName(id, newName)
    }

    fun getCategoryIdByName(name: String): Long? {
        return db.getCategoryIdByName(name)
    }
}

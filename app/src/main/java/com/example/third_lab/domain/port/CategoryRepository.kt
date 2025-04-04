package com.example.third_lab.domain.port

import com.example.third_lab.domain.entity.Category

interface CategoryRepository {

    fun getCategories(): List<Category>

    fun addCategory(name: String): Long

    fun deleteCategory(id: Long): Int

    fun updateCategoryName(id: Long, newName: String): Int

    fun getCategoryIdByName(name: String): Long?
}